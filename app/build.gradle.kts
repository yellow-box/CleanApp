plugins {
    alias(libs.plugins.android.gradle.application)
    alias(libs.plugins.kotlin.android.plugin)
}

android {
    namespace = "com.example.cleanapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cleanapp"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.android)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.junit.androidx)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":datalib"))

    implementation(project(":domain"))
    implementation(project(":datalib"))
    implementation(project(":platformrelated"))
}