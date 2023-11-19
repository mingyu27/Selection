plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")


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
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.kakao.sdk:v2-all:2.17.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation ("com.kakao.sdk:v2-user:2.17.0") // 카카오 로그인
    implementation ("com.kakao.sdk:v2-talk:2.17.0") // 친구, 메시지(카카오톡)
    implementation ("com.kakao.sdk:v2-story:2.17.0") // 카카오스토리
    implementation ("com.kakao.sdk:v2-share:2.17.0") // 메시지(카카오톡 공유)
    implementation ("com.kakao.sdk:v2-friend:2.17.0") // 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation ("com.kakao.sdk:v2-navi:2.17.0") // 카카오내비
    implementation ("com.kakao.sdk:v2-cert:2.17.0") // 카카오 인증서비스
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.core:core-splashscreen:1.0.0-alpha01") //splash

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-functions:20.4.0")


//    //카카오 파이어베이스 연동
//    implementation ("com.kakao.sdk:v2-user-rx:2.0.0-beta02")
//    implementation ("io.reactivex.rxjava2:rxjava:2.2.17")
//    implementation ("com.squareup.retrofit2:converter-gson:2.7.1")
//    implementation ("com.squareup.retrofit2:retrofit:2.7.1")
//    implementation ("com.squareup.okhttp3:okhttp:4.3.1")
//    implementation ("com.squareup.okhttp3:logging-interceptor:4.3.1")
//    implementation ("com.google.firebase:firebase-auth:19.1.0")
//    implementation ("com.google.firebase:firebase-firestore:21.4.0")
//    implementation ("com.google.firebase:firebase-firestore-ktx:21.4.0")
//    implementation ("org.koin:koin-androidx-viewmodel:2.0.1")

    

}