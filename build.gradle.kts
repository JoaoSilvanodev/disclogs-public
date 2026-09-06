// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:23.0.0")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.dagger.hilt.android") version "2.60.1" apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
