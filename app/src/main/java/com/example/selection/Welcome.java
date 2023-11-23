package com.example.selection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.selection.databinding.ActivityWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import java.util.HashMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Welcome extends AppCompatActivity {
    private ActivityWelcomeBinding binding;
    private View loginWithKakaoButton, loginButton, joinButton;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private static final String TAG = "SMG";
    private String userName;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            startActivity(new Intent(Welcome.this, MainActivity.class).putExtra("user", currentUser));
            finish();
        }

    }

    //getCustomToken() 실행
    Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if (oAuthToken != null) {
                Log.d("TOKEN", oAuthToken.getAccessToken());
                getCustomToken(oAuthToken.getAccessToken());}
            if (throwable != null) {
                Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());}

            updateKakaoInfo();
            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        loginWithKakaoButton = binding.loginWithKakaoButton;
        loginButton = binding.loginWithLocal;
        joinButton = binding.joinWithLocal;


        loginButton.setOnClickListener(v -> {startActivity(new Intent(Welcome.this, WelcomeLogin.class));});

        joinButton.setOnClickListener(v -> {startActivity(new Intent(Welcome.this, WelcomeJoin.class));});

        //카카오톡 or 카카오계정으로 로그인
        loginWithKakaoButton.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(Welcome.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(Welcome.this, callback);
            } else {UserApiClient.getInstance().loginWithKakaoAccount(Welcome.this, callback);}
        });



//        로그아웃버튼
//        logoutButton.setOnClickListener(view -> UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
//            @Override
//            public Unit invoke(Throwable throwable) {
//                updateKakaoLoginUi();
//                return null;
//            }
//        }));
    }





    private void getCustomToken(String accessToken) {
        FirebaseFunctions functions = FirebaseFunctions.getInstance("asia-northeast3");
        HashMap<String, Object> data = new HashMap<>();
        data.put("token", accessToken);

        functions
                .getHttpsCallable("kakaoCustomAuth")
                .call(data)
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                        try {
                            // 호출 성공
                            HashMap<String, Object> result = (HashMap<String, Object>) task.getResult().getData();
                            String mKey = null;
                            for (String key : result.keySet()) {
                                mKey = key;
                            }
                            String customToken = result.get(mKey).toString();
                            // 호출 성공해서 반환받은 커스텀 토큰으로 Firebase Authentication 인증받기
                            firebaseAuthWithKakao(customToken);
                        } catch (RuntimeExecutionException e) {
                            // 호출 실패
                        }
                    }
                });
    }

    private void firebaseAuthWithKakao(String customToken) {
        mAuth.signInWithCustomToken(customToken).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Firebase Authentication 인증 성공 후 로직
                updateUI(currentUser);
            } else {
                // 실패 후 로직
            }
        });
    }


    private void updateUI(FirebaseUser user){
        startActivity(new Intent(Welcome.this, AddCardAlert.class).putExtra("userName", userName));
    }


    //userName 초기화
    private void updateKakaoInfo() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                userName = user.getKakaoAccount().getProfile().getNickname();
//                    Log.i(TAG, "invoke: id=" + user.getId());
//                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
//                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
//                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
//                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

            } else {

            }
            if (throwable != null) {
                Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());
            }
            return null;
        });

    }


}
