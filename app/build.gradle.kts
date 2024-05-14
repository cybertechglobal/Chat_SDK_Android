plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val chatAppTokenDev = project.properties["CHAT_APP_TOKEN_DEV"]
val chatAppTokenProd=project.properties["CHAT_APP_TOKEN_PROD"]

android {
    signingConfigs {
        create("release") {
            storeFile = file("signkey.jks")
            storePassword = "sekiseki"
            keyAlias = "sekiseki"
            keyPassword = "sekiseki"
        }
    }
    namespace = "eu.brrm.chattestapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "eu.brrm.chattestapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug{
            buildConfigField(type = "String", name = "CHAT_APP_TOKEN", "\"${chatAppTokenDev}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField(type = "String", name = "CHAT_APP_TOKEN", "\"${chatAppTokenProd}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures{
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.webkit:webkit:1.11.0")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    //implementation("com.google.firebase:firebase-analytics-ktx")

    implementation(project(":ChatUI"))
    //implementation("eu.brrm:chat-ui:1.0.0-SNAPSHOT") //"eu.brrm:chat-ui:1.0.0-SNAPSHOT"
    //implementation("com.github.cybertechglobal:Chat_SDK_Android:1.0.8")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}