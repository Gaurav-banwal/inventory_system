plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
      id("com.google.gms.google-services")
}




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

    
        implementation("com.google.firebase:firebase-auth:22.0.0")





//    implementation(libs.androidx.room.runtime)
//    ksp(libs.androidx.room.compiler)
//
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    kapt("androidx.room:room-compiler:2.6.1")
//    ksp("androidx.room:room-compiler:2.6.1")

//implementation("com.google.firebase:firebase-auth:23.2.0")
//    implementation("com.google.firebase:firebase-auth-ktx:22.0.0")
//    implementation("com.google.firebase:firebase-database-ktx:20.0.7")
    implementation ("com.google.firebase:firebase-firestore:25.1.3")
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


