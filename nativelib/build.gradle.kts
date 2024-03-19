plugins {
    id("com.android.library")
}

android {
    namespace = "com.example.nativelib"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}