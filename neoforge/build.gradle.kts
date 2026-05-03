import net.fabricmc.loom.api.LoomGradleExtensionAPI

val minecraftVersion: String = stonecutter.current.version
val isUnobfuscated = stonecutter.eval(minecraftVersion, ">=26.1")

plugins {
    java
    // 26.1+ is unobfuscated; needs the no-remap variant. Older versions need the
    // standard plugin so it can apply official Mojang mappings. Both expose a
    // 'loom' extension of type LoomGradleExtensionAPI, so the rest of the script
    // works identically either way.
    id("dev.architectury.loom") apply false
    id("dev.architectury.loom-no-remap") apply false
}

apply(plugin = if (isUnobfuscated) "dev.architectury.loom-no-remap" else "dev.architectury.loom")

val branchRoot = projectDir.resolve("../..")

version = "${mod.version}+$minecraftVersion-neoforge"
base.archivesName.set("${mod.id}-neoforge")

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
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

sourceSets["main"].apply {
    java.setSrcDirs(listOf(branchRoot.resolve("src/main/java")))
    resources.setSrcDirs(listOf(branchRoot.resolve("src/main/resources")))
}

val loom = extensions.getByName<LoomGradleExtensionAPI>("loom")

loom.silentMojangMappingsLicense()

dependencies {
    "minecraft"("com.mojang:minecraft:$minecraftVersion")
    if (!isUnobfuscated) {
        "mappings"(loom.officialMojangMappings())
    }
    "neoForge"("net.neoforged:neoforge:${mod.dep("neoforge")}")
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
        "minecraft_version_range" to mod.dep("minecraft_range"),
        "neo_version" to mod.dep("neoforge"),
        "neo_version_range" to mod.dep("neoforge_range"),
        "loader_version_range" to mod.dep("loader_range"),
    )
    for ((k, v) in tokens) inputs.property(k, v)
    filesMatching("META-INF/neoforge.mods.toml") { expand(tokens) }
}
