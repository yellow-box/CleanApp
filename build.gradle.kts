// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.gradle.application) apply false
    alias(libs.plugins.android.gradle.library) apply false
    alias(libs.plugins.kotlin.android.plugin) apply  false
    alias(libs.plugins.kotlin.jvm.plugin) apply false
}