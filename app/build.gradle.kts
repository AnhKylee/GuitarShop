plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.guitarshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.guitarshop"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // Retrofit for API Calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room Database (Optional for Local Storage)
    implementation("androidx.room:room-runtime:2.5.0")
    //kapt("androidx.room:room-compiler:2.5.0")

    // Firebase Authentication (for OTP & Email Login)
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-auth")

    // ZaloPay SDK (For payments)
    implementation("vn.zalopay.sdk:zalopay-sdk:1.0.3")

    // Google Maps (Optional)
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // Notifications
    implementation("com.google.firebase:firebase-messaging")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}