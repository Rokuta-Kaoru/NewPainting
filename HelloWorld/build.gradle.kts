// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id ("com.chaquo.python") version "15.0.1" apply  false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven { url = uri("${projectDir}/OpenCV-android-sdk/sdk") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.4.1")
        classpath("com.chaquo.python:gradle:10.0.1")  // ここにChaquopyを追加
    }
}