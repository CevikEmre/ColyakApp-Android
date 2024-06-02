plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.colyak"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.colyak"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//noinspection UseTomlInstead
dependencies {
    //core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    //compose
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")

    //retrofit & Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")


    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation(libs.protolite.well.known.types)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.room:room-coroutines:2.1.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation("com.android.support:multidex:1.0.3")
    implementation("androidx.compose.foundation:foundation-layout-android:1.6.7")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.coil-kt:coil-compose:1.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //cameraX
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")

    //barcode
    implementation("com.google.mlkit:barcode-scanning:17.2.0")
    //Camera Permission
    implementation("com.google.accompanist:accompanist-permissions:0.19.0")

    //PDF
    //implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation("io.github.grizzi91:bouquet:1.1.2")
}