plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.acm.newtokoalarm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.acm.newtokoalarm"
        minSdk = 28
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    //ui
    implementation(libs.core.splashscreen)
    implementation(libs.intuit.sdp.android)
    implementation(libs.intuit.ssp.android)
    implementation(libs.android.lottie)

    //core
    implementation(libs.retrofit2.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation(libs.github.glide)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.jwtdecode)

    //common
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}