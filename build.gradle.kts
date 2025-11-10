plugins {
    java
    `maven-publish`
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
}

architectury {
    minecraft = stonecutter.current.version
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "maven-publish")

    version = rootProject.extra["mod_version"]!!
    group = rootProject.extra["mod_group_id"]!!

    base {
        archivesName.set("${rootProject.extra["mod_id"]}-${project.name}")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
    }

    configure<net.fabricmc.loom.api.LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:${stonecutter.current.version}")
        "mappings"(the<net.fabricmc.loom.api.LoomGradleExtensionAPI>().officialMojangMappings())
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
        withSourcesJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

// Define shared properties
extra["mod_version"] = "1.21-5.0.0"
extra["mod_group_id"] = "dev.mja00.alphaskins"
extra["mod_id"] = "alphaskins"
extra["mod_name"] = "Alphaskins"
extra["mod_license"] = "GPL3"
extra["mod_authors"] = "mja00"
extra["mod_description"] = "Enable transparency in skins"
