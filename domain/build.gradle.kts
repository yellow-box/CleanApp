plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.devtools.ksp)
//    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies{
    implementation(libs.gson)
    implementation(libs.coroutine.core)
    api(project(":domain:libannotation"))
    ksp(project(":domain:libannotation"))
}