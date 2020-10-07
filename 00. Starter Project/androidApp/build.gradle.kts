plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}
group = "com.cmota.playground.alltogethernow"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
}
dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("com.google.android.gms:play-services-auth:18.1.0")

    implementation("androidx.appcompat:appcompat:1.2.0")

    implementation("com.google.firebase:firebase-firestore-ktx:21.7.0")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation("com.russhwolf:multiplatform-settings:0.6.2")
}
android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.cmota.playground.alltogethernow.androidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
}