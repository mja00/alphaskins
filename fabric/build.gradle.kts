plugins {
    java
    id("dev.architectury.loom")
}

val minecraftVersion: String = stonecutter.current.version
val branchRoot = projectDir.resolve("../..")

version = "${mod.version}+$minecraftVersion-fabric"
base.archivesName.set("${mod.id}-fabric")

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
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

// Source lives in fabric/src/main/, not fabric/versions/<v>/src/.
sourceSets["main"].apply {
    java.setSrcDirs(listOf(branchRoot.resolve("src/main/java")))
    resources.setSrcDirs(listOf(branchRoot.resolve("src/main/resources")))
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api")}")
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
    )
    for ((k, v) in tokens) inputs.property(k, v)
    filesMatching("fabric.mod.json") { expand(tokens) }
}
