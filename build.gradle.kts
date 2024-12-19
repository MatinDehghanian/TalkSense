// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // Add this line to include JitPack repository
    }
}

subprojects {
    // Add dependencies to each subproject
    project.afterEvaluate {
        dependencies {
            // Correct way to add dependencies
            add("implementation", "com.github.JorenSix:TarsosDSP:2.4") // or "be.tarsos:dsp:2.4.4" if preferred
        }
    }
}