pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.+"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true

    create(rootProject) {
        // No common branch — each loader compiles its own copy of the mixins.
        branch("fabric") {
            versions("1.21.4", "1.21.8", "1.21.11", "26.1.2", "26.2")
        }
        branch("neoforge") {
            versions("1.21.4", "1.21.8", "1.21.11", "26.1.2", "26.2")
        }
    }
}

rootProject.name = "alphaskins"
