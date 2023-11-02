package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.selection.databinding.ActivityLoginBinding;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private ActivityLoginBinding binding;
    private View loginButton, logoutButton;
    private TextView nickname;
    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginButton = binding.kakaoLogin;
        logoutButton = binding.logout;
        nickname = binding.nickname;
        profileImage = binding.profile;



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
                Login.this.updateKakaoLoginUi();
                return null;
            }
        };

        loginButton.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(Login.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(Login.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(Login.this, callback);
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
                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
                    nickname.setText(user.getKakaoAccount().getProfile().getNickname());
                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(binding.profile);
                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                } else {
                    nickname.setText(null);
                    profileImage.setImageBitmap(null);
                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
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
//sng22
///smg33