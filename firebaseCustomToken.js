const admin = require('firebase-admin');

// 다운로드한 서비스 계정 키의 경로를 설정합니다.
const serviceAccount = require('./selection-7cede-firebase-adminsdk-qph9x-4cd64346d5.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  // 나머지 초기화 옵션들을 필요에 따라 추가할 수 있습니다.
});

const uid = 'some-uid'; // 사용자 UID
const additionalClaims = {
  premiumAccount: true, // 추가적인 클레임
};

admin.auth().createCustomToken(uid, additionalClaims)
  .then((customToken) => {
    console.log('Custom token:', customToken);
  })
  .catch((error) => {
    console.error('Error creating custom token:', error);
  });
