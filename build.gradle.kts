
// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {

    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}