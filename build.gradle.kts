//// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "8.2.0-beta06" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
//buildscript {
//    ext {
//        compose_version = "1.6.0-alpha07"
//        compilerVersion = "1.5.3"
//    }
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath "com.android.tools.build:gradle:8.1.2"
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10"
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        maven {
//            url "https://kotlin.bintray.com/kotlinx"
//        }
//    }
//}
//
//tasks.register('clean', Delete) {
//    delete rootProject.buildDir
//}