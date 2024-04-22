// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

apply("${rootDir}/scripts/publish-root.gradle")