buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.10")
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.3")
    }
}
group = "com.cmota.playground.alltogethernow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
