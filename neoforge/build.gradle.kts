plugins {
    java
    id("dev.architectury.loom")
}

val minecraftVersion: String = stonecutter.current.version
val branchRoot = projectDir.resolve("../..")

version = "${mod.version}+$minecraftVersion-neoforge"
base.archivesName.set("${mod.id}-neoforge")

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.architectury.dev/")
}

val javaVersion = if (stonecutter.eval(minecraftVersion, ">=26.1")) JavaVersion.VERSION_25
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

loom {
    silentMojangMappingsLicense()
    accessWidenerPath.set(branchRoot.resolve("src/main/resources/alphaskins.accesswidener"))
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
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
