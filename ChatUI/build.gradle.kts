@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}


android {
    namespace = "eu.brrm.chatui"
    compileSdk = 34

    val DEV_BRRM_HOST: String = "https://devwidget.chat.brrm.eu"
    val PROD_BRRM_HOST: String = "https://widget.chat.brrm.eu"

    val DEV_CHAT_URL: String = "https://devapi.chat.brrm.eu/"
    val PROD_CHAT_URL: String = "https://api.chat.brrm.eu/"
    defaultConfig {
        minSdk = 21
        version = 3
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 21
        }
    }

    buildTypes {
        debug {
            //resValue("string", "BRRM_CHAT_HOST", DEV_BRRM_HOST)
            resValue("string", "BRRM_CHAT_BASE_URL", DEV_BRRM_HOST)
            resValue("string", "CHAT_URL", DEV_CHAT_URL)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //resValue("string", "BRRM_CHAT_HOST", PROD_BRRM_HOST)
            resValue("string", "BRRM_CHAT_BASE_URL", PROD_BRRM_HOST)
            resValue("string", "CHAT_URL", PROD_CHAT_URL)
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