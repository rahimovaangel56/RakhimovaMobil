// build.gradle.kts (Project: RakhimovaKP)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false

    id("androidx.navigation.safeargs.kotlin") version "2.9.6" apply false
}