@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}


android {
    namespace = "eu.brrm.chatui"
    compileSdk = 34
    val DEV_BRRM_HOST: String = "testwidget.myautohouse.eu"
    val PROD_BRRM_HOST: String = "testwidget.myautohouse.eu"
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
            resValue("string", "BRRM_CHAT_HOST", DEV_BRRM_HOST)
            resValue("string", "BRRM_CHAT_BASE_URL", "https://${DEV_BRRM_HOST}/")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "BRRM_CHAT_HOST", PROD_BRRM_HOST)
            resValue("string", "BRRM_CHAT_BASE_URL", "https://${PROD_BRRM_HOST}/")
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
                    groupId = "le"
                    artifactId = "chat-ui"
                    version = "1.0.3"

                    from(components["release"])
                }
            }
        }
    }
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.webkit:webkit:1.10.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-messaging-directboot")

    implementation("androidx.datastore:datastore-preferences:1.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}