// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.module.graph) apply true
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

// Task to print all the module paths in the project e.g. :core:data
// Used by module graph generator script
tasks.register("printModulePaths") {
    subprojects {
        if (subprojects.size == 0) {
            println(this.path)
        }
    }
}