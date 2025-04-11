plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.androidApplication)
//    alias(libs.plugins.kotlinAndroid)
//    alias(libs.plugins.kotlinKapt)
//    alias(libs.plugins.ksp)
//
//
//
//
//
//
//        id("com.android.application")
//        id("org.jetbrains.kotlin.android")
//        id("com.google.devtools.ksp") // KSP plugin
//    id("kotlin-kapt") // Required for Room's annotation processing




//    id("com.android.application")
//        id("org.jetbrains.kotlin.android")

//// This is necessary for Room


}

//
//repositories {
//    google()
//    mavenCentral()
//}



android {
    namespace = "com.oops.inventory_system"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.oops.inventory_system"
        minSdk = 22
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {



//    implementation(libs.androidx.room.runtime)
//    ksp(libs.androidx.room.compiler)
//
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    kapt("androidx.room:room-compiler:2.6.1")
//    ksp("androidx.room:room-compiler:2.6.1")




    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


