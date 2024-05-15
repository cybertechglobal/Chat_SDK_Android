@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}


android {
    namespace = "eu.brrm.chatui"
    compileSdk = 34

    val CHAT_WIDGET_URL_DEV: String = "https://devwidget.chat.brrm.eu"
    val CHAT_WIDGET_URL_PROD: String = "https://widget.chat.brrm.eu"

    val CHAT_API_DEV: String = "https://devapi.chat.brrm.eu/"
    val CHAT_API_PROD: String = "https://api.chat.brrm.eu/"


    defaultConfig {
        minSdk = 21
        version = 3
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 21
        }

        resValue("string", "CHAT_WIDGET_URL_DEV", CHAT_WIDGET_URL_DEV)
        resValue("string", "CHAT_WIDGET_URL_PROD", CHAT_WIDGET_URL_PROD)
        resValue("string", "CHAT_API_DEV", CHAT_API_DEV)
        resValue("string", "CHAT_API_PROD", CHAT_API_PROD)

    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testFixtures {
        enable = true
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
        }
    }

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = "eu.brrm"
                    artifactId = "chat-ui"
                    version = "1.0.0-SNAPSHOT"

                    from(components["release"])
                }
            }
        }
    }
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.webkit:webkit:1.11.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-messaging-directboot")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}