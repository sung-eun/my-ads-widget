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
        summary = "AdSense Data Shared Module"
        homepage = ""
        ios.deploymentTarget = "14.1"
        frameworkName = "feature:adsense:data"
        // set path to your ios project podfile, e.g. podfile = project.file("../iosApp/Podfile")
    }

    val ktorVersion = "1.6.1"
    val kvaultVersion = "1.7.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("com.liftric:kvault:$kvaultVersion")
                implementation(project(":common:domain"))
            }
        }
        val androidMain by getting
        val iosMain by getting
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