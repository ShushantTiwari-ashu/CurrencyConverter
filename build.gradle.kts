// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.sonarqube)
}
sonarqube {
    properties {
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.projectKey", "CurrencyConverter")
        property("sonar.projectName", "CurrencyConverter")
        property("sonar.token", "sqp_6a9e43efaa95b3609e5b483efd3fbdbf9c63a491")
    }
}
