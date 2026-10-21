plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.22"

    // KSP plugin and Room 3 plugin matching lecture
    id("com.google.devtools.ksp") version "2.3.11"
    id("androidx.room3") version "3.0.3"
}

// Point schemaDirectory to a schemas folder in the project
room3 {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.example.mad511_lab1_dakhlallah_ali"
    compileSdk = 36 // Updated to 36 to support Navigation 3 and activity-compose alpha dependencies

    defaultConfig {
        applicationId = "com.example.mad511_lab1_dakhlallah_ali"
        minSdk = 26
        targetSdk = 35
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

    // Navigation 3 Dependencies
    implementation("androidx.navigation3:navigation3-runtime:1.0.0-alpha02")
    implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha02")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // ViewModel & Lifecycle for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Room 3 runtime, compiler, and bundled driver from lecture
    implementation("androidx.room3:room3-runtime:3.0.3")
    implementation("androidx.sqlite:sqlite-bundled:2.7.1")
    ksp("androidx.room3:room3-compiler:3.0.3")
}