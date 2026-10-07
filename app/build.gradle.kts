plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.example.mad511_lab1_dakhlallah_ali"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.mad511_lab1_dakhlallah_ali"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation 3 Dependencies (Updated to alpha02)
    implementation("androidx.navigation3:navigation3-runtime:1.0.0-alpha02")
    implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha02")

    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:2.9.0-alpha10")
    implementation("androidx.navigation3:navigation3-saveable-state:1.0.0-alpha02")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // ViewModel & Lifecycle for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended")
}