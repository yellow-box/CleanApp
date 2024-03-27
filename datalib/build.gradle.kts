plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    alias(libs.plugins.android.gradle.library)
}

android {
    namespace = "com.example.datalib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags("")
                //只编译 arm64-v8架构的 abi
                abiFilters("arm64-v8a")
            }
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}
//tasks.withType<JavaCompile> {
//    options.compilerArgs.addAll(listOf(
//        "--enable-preview",
//        "--add-modules=jdk.incubator.vector"
//    ))
//}

//tasks.withType<JavaExec>() {
//    jvmArgs("--enable-preview", "-Djava.library.path=./lib", "--add-modules=jdk.incubator.vector")
//}
dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core)
    implementation(libs.datastore.preferences)// 使用类似 SharedPreferences APi
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.androidx)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.androidx)
    androidTestImplementation(libs.espresso.core)
}
