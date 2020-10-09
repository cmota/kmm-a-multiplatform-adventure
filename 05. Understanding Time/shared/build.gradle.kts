import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-android-extensions")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
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
sqldelight {
    database("ConferenceDb") {
        packageName = "data"
    }
}
kotlin {
    android()

    val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
    if (onPhone) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // You can change the name of the produced framework.
        // By default, it is the name of the Gradle project.
        frameworkName = "shared"

        ios.deploymentTarget = "13.2"

        pod("FirebaseFirestore")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:1.4.1")
                implementation("io.ktor:ktor-client-json:1.4.1")
                implementation("io.ktor:ktor-client-serialization:1.4.1")
                implementation("io.ktor:ktor-client-logging:1.4.1")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9-native-mt-2") {
                    version {
                        strictly("1.3.9-native-mt-2")
                    }
                }
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.0")

                implementation("com.google.firebase:firebase-firestore-ktx:21.7.0")

                implementation("com.squareup.sqldelight:runtime:1.4.3")

                implementation("com.russhwolf:multiplatform-settings:0.6.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")

                implementation("androidx.preference:preference:1.1.1")

                implementation("io.ktor:ktor-client-android:1.4.1")
                implementation("io.ktor:ktor-client-okhttp:1.4.1")

                implementation("com.squareup.sqldelight:android-driver:1.4.3")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:1.4.1")

                implementation("com.squareup.sqldelight:native-driver:1.4.3")
            }
        }
        val iosTest by getting
    }
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
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
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)