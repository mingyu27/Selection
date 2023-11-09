package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.selection.databinding.ActivityLoginBinding;
import com.example.selection.databinding.ActivityWelcomeBinding;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class Welcome extends AppCompatActivity {
    private static final String TAG = "Login";
    private ActivityWelcomeBinding binding;
    private View loginWithKakaoButton;
    private View logoutButton;
    private View loginButton, joinButton;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String kakaoHashKey = KakaoSdk.INSTANCE.getKeyHash();
        Log.d("kakaoHashKey", kakaoHashKey);
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        setContentView(binding.getRoot());
        loginWithKakaoButton = binding.loginWithKakaoButton;
        logoutButton = binding.logout;
        loginButton = binding.loginWithLocal;
        joinButton = binding.joinWithLocal;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, LoginPage.class));
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, Join.class));
            }
        });

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    // TBD
                }
                if (throwable != null) {
                    // TBD
                    Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());
                }
                Welcome.this.updateKakaoLoginUi();

                return null;
            }
        };

        loginWithKakaoButton.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(Welcome.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(Welcome.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(Welcome.this, callback);
                //
            }



        });




        logoutButton.setOnClickListener(view -> UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                updateKakaoLoginUi();
                return null;
            }
        }));

        updateKakaoLoginUi();

    }




    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    userName = user.getKakaoAccount().getProfile().getNickname();
                    startActivity(new Intent(Welcome.this, MainActivity.class).putExtra("userName", userName));
                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
                    binding.welcomeText.setText(userName +" 로그인됨");
//                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(binding.profile);
                    binding.loginWithKakaoButton.setVisibility(View.GONE);
                    binding.loginWithLocal.setVisibility(View.GONE);
                    binding.joinWithLocal.setVisibility(View.GONE);
                    binding.logout.setVisibility(View.VISIBLE);
                } else {
                    binding.welcomeText.setText("안녕하세용?");
                    binding.loginWithKakaoButton.setVisibility(View.VISIBLE);
                    binding.loginWithLocal.setVisibility(View.VISIBLE);
                    binding.joinWithLocal.setVisibility(View.VISIBLE);
                    binding.logout.setVisibility(View.GONE);
                }
                if (throwable != null) {
                    Log.d("TAG", "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }
}

//smg