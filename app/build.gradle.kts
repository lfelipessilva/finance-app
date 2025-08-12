import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.orbita.finad"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.orbita.finad"
        minSdk = 31
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        val properties = Properties()

        val localPropertiesFile = project.rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${properties.getProperty("GOOGLE_CLIENT_ID", "")}\"")
        buildConfigField("String", "BACKEND_URL", "\"${properties.getProperty("BACKEND_URL", "")}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    // implementation(libs.androidx.appcompat) // Not needed for Compose-only apps
    // implementation(libs.material) // Not needed if using Material3
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // Room dependencies
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.room.compiler)
    // Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)
    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    // Coroutines
    implementation(libs.coroutines.android)
    // Google sign-in and credentials
    implementation(libs.play.services.auth)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Only once
    implementation(libs.androidx.credentials.v150)
    implementation(libs.androidx.credentials.play.services.auth.v150)
    implementation(libs.googleid.v111)
}