package com.example.selection;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        KakaoSdk.init(this, "4e903e38c7d87f7aa16d77518fc085a9");
        super.onCreate();
    }
}
