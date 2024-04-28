@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm.plugin)

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies{
//    implementation(libs.devtools.ksp)
    implementation(libs.devtools.ksp.symbol.processing.api)
    //kotlin 源文件的代码生成框架
    implementation(libs.kotlinpoet.ksp)
//    implementation(libs.room.compiler)
}

//plugins {
//    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
//}
//
//android {
//    namespace = "com.example.domain.libannotation"
//    compileSdk = 34
//
//    defaultConfig {
//        minSdk = 24
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//
//
//dependencies {
////    implementation(libs.devtools.ksp)
//    implementation(libs.devtools.ksp.symbol.processing.api)
//    //kotlin 源文件的代码生成框架
//    implementation(libs.kotlinpoet.ksp)
////    implementation(libs.room.compiler)
//}

