import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.5.20"
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        framework {
            summary = "AdSense Data Shared Module"
            homepage = "https://essie-cho.com"
            ios.deploymentTarget = "14.1"
            baseName = "AdSenseData"
            license = "Copyright (C) 2021 by essie-cho"

            export(project(":common:domain"))
        }
    }

    val ktorVersion = "1.6.1"
    val kvaultVersion = "1.7.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
//                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("com.liftric:kvault:$kvaultVersion")

                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")

                api(project(":common:domain"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(31)
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}