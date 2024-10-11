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
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.chaquo.python:gradle:10.0.1")  // ここにChaquopyを追加
    }
}