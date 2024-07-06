plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.composeapplicationabc"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlin {
        sourceSets.all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
    }

    kotlinOptions {
        jvmTarget = "19"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    namespace = "com.example.composeapplication"
}

dependencies {
    val composeVersion = "1.6.8"
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    val livecycleVersion = "2.8.3"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$livecycleVersion")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$livecycleVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$livecycleVersion")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    val accompanistVersion = "0.34.0"
    implementation("com.google.accompanist:accompanist-webview:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    implementation("androidx.core:core-splashscreen:1.0.1")
    // 替代sharepreference
    val preferencesVersion = "1.1.1"
    implementation("androidx.datastore:datastore-preferences:$preferencesVersion")
    implementation("androidx.datastore:datastore-preferences-core:$preferencesVersion")
    implementation("androidx.paging:paging-compose:3.3.0")
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")

    val cameraxVersion = "1.3.4"
    // CameraX core library using camera2 implementation
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    // CameraX Lifecycle Library
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    // CameraX View class
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.compose.material3:material3:1.2.1")
    val version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$version")
    implementation("androidx.navigation:navigation-fragment-ktx:$version")
    implementation("androidx.navigation:navigation-ui-ktx:$version")
    implementation("com.airbnb.android:lottie-compose:6.4.1")

    val media3version = "1.3.1"
    implementation("androidx.media3:media3-exoplayer:$media3version")
    implementation("androidx.media3:media3-ui:$media3version")
    implementation("androidx.media3:media3-session:$media3version")
}