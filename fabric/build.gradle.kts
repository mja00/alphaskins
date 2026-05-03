import net.fabricmc.loom.api.LoomGradleExtensionAPI

val minecraftVersion: String = stonecutter.current.version
val isUnobfuscated = stonecutter.eval(minecraftVersion, ">=26.1")

plugins {
    java
    // 26.1+ is unobfuscated; needs FabricMC's no-remap plugin variant which
    // skips Mojang mapping resolution. Older versions stay on Architectury Loom.
    id("dev.architectury.loom") apply false
    id("net.fabricmc.fabric-loom-no-remap") version "1.14.0-alpha.31" apply false
}

apply(plugin = if (isUnobfuscated) "net.fabricmc.fabric-loom-no-remap" else "dev.architectury.loom")

val branchRoot = projectDir.resolve("../..")

version = "${mod.version}+$minecraftVersion-fabric"
base.archivesName.set("${mod.id}-fabric")

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
}

val javaVersion = if (isUnobfuscated) JavaVersion.VERSION_25
    else if (stonecutter.eval(minecraftVersion, ">=1.20.5")) JavaVersion.VERSION_21
    else JavaVersion.VERSION_17

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.majorVersion.toInt()))
    withSourcesJar()
}

// Source lives in fabric/src/main/, not fabric/versions/<v>/src/.
sourceSets["main"].apply {
    java.setSrcDirs(listOf(branchRoot.resolve("src/main/java")))
    resources.setSrcDirs(listOf(branchRoot.resolve("src/main/resources")))
}

val loom = extensions.getByName<LoomGradleExtensionAPI>("loom")
if (!isUnobfuscated) {
    // Reflective: fabric-loom 1.16's API doesn't expose this method (no mappings),
    // so we can't reference it statically — the script wouldn't compile under that
    // plugin variant.
    loom.javaClass.getMethod("silentMojangMappingsLicense").invoke(loom)
}

dependencies {
    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    if (isUnobfuscated) {
        // No mappings, no mod-remapping — fabric-loom-no-remap treats deps as
        // already-resolved jars.
        "implementation"("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        "implementation"("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api")}")
    } else {
        "mappings"(loom.officialMojangMappings())
        "modImplementation"("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
        "modImplementation"("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api")}")
    }
}

tasks.processResources {
    val tokens = mapOf(
        "mod_id" to mod.id,
        "mod_name" to mod.name,
        "mod_version" to mod.version,
        "mod_description" to mod.description,
        "mod_authors" to mod.author,
        "mod_license" to mod.license,
        "minecraft_version" to minecraftVersion,
        "java_version" to javaVersion.majorVersion,
    )
    for ((k, v) in tokens) inputs.property(k, v)
    filesMatching("fabric.mod.json") { expand(tokens) }
}
