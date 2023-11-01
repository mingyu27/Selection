plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.selection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.selection"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    viewBinding{
        enable = true;
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.kakao.sdk:v2-user:2.8.6") // 카카오 로그인
    implementation ("com.kakao.sdk:v2-talk:2.6.0") // 친구, 메시지(카카오톡)
    implementation ("com.kakao.sdk:v2-story:2.6.0") // 카카오스토리
    implementation ("com.kakao.sdk:v2-link:2.6.0") // 메시지(카카오링크)
    implementation ("com.kakao.sdk:v2-navi:2.6.0") // 카카오 내비
// 오픈소스 이미지 핸들링할 때 많이 사용되는 글라이드
    implementation ("com.github.bumptech.glide:glide:4.11.0")
}