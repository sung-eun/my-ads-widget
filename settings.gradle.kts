pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "myads"
include(":ios")
include(":aos")
include(":feature:adsense:data")
include(":common:domain")