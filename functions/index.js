const functions = require("firebase-functions");
const request = require("request-promise");
const admin = require("firebase-admin");
const serviceAccount = require("./admin.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const kakaoRequestMeUrl = "https://kapi.kakao.com/v2/user/me";

/**
 * Kakao API 서버에서 사용자 프로필을 요청합니다.
 * @param {string} kakaoAccessToken - Kakao Access Token
 * @return {Promise<string>} - Kakao API 서버의 응답을 포함한 Promise
 */
function requestMe(kakaoAccessToken) {
  console.log("Requesting user profile from Kakao API server.");
  return request({
    method: "GET",
    headers: { "Authorization": `Bearer ${kakaoAccessToken}` },
    url: kakaoRequestMeUrl,
  });
}

/**
 * Firebase 사용자를 업데이트하거나 만듭니다.
 * @param {string} userId - Firebase 사용자 ID
 * @param {string} email - 사용자 이메일
 * @param {string} displayName - 사용자의 표시 이름
 * @param {string} photoURL - 사용자의 프로필 사진 URL
 * @return {Promise<admin.auth.UserRecord>} - Firebase 사용자 레코드를 포함한 Promise
 */
function updateOrCreateUser(userId, email, displayName, photoURL) {
  console.log("Updating or creating a Firebase user");
  const updateParams = {
    provider: "KAKAO",
    displayName,
    email,
  };
  if (email) {
    updateParams.displayName = email;
  } else {
    updateParams.displayName = displayName;
  }
  if (photoURL) {
    updateParams.photoURL = photoURL;
  }
  console.log(updateParams);
  return admin.auth().updateUser(userId, updateParams)
      .catch((error) => {
        if (error.code === "auth/user-not-found") {
          updateParams.uid = userId;
          if (email) {
            updateParams.email = email;
          }
          return admin.auth().createUser(updateParams);
        }
        throw error;
      });
}

/**
 * Kakao Access Token을 사용하여 Firebase Custom Token을 생성합니다.
 * @param {string} kakaoAccessToken - Kakao Access Token
 * @return {Promise<string>} - Firebase Custom Token을 포함한 Promise
 */
function createFirebaseToken(kakaoAccessToken) {
  return requestMe(kakaoAccessToken).then((response) => {
    const body = JSON.parse(response);
    console.log(body);
    const userId = `kakao:${body.id}`;
    if (!userId) {
      throw new functions.https.HttpsError(
          "invalid-argument",
          "Not response: Failed to get userId",
      );
    }

    let nickname = null;
    let profileImage = null;
    let email = null;
    if (body.properties) {
      nickname = body.properties.nickname;
      profileImage = body.properties.profile_image;
    }
    if (body.kakao_account) {
      email = body.kakao_account.email;
    }
    return updateOrCreateUser(
        userId,
        email,
        nickname,
        profileImage,
    );
  }).then((userRecord) => {
    const userId = userRecord.uid;
    console.log(`Creating a custom Firebase token based on uid ${userId}`);
    return admin.auth().createCustomToken(userId, { provider: "KAKAO" });
  });
}

/**
 * Kakao Custom Authentication을 수행하는 Firebase Cloud Function
 * @param {Object} data - 클라이언트에서 전달한 데이터 객체
 * @param {string} data.token - Kakao Access Token
 * @returns {Promise<Object>} - 클라이언트에 반환되는 Promise
 */
exports.kakaoCustomAuth = functions.region("asia-northeast3").https
    .onCall((data) => {
      const { token } = data;

      if (!(typeof token === "string") || token.length === 0) {
        throw new functions.https.HttpsError("invalid", "Must be called with " +
                "one argument \"data\" containing the token.");
      }

      console.log(`Verifying Kakao token: ${token}`);

      return createFirebaseToken(token).then((firebaseToken) => {
        console.log(`Returning Firebase token to user: ${firebaseToken}`);
        return { "custom_token": firebaseToken };
      });
    });
