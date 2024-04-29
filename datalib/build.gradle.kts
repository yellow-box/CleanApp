plugins {
//    id("com.android.library")
    alias(libs.plugins.android.gradle.library)
    alias(libs.plugins.kotlin.android.plugin)
    alias(libs.plugins.devtools.ksp)
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
    implementation(libs.material)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
//    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")//引入ksp api
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.androidx)
    androidTestImplementation(libs.espresso.core)
}
