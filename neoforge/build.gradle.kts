architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    accessWidenerPath.set(project(":common").file("src/main/resources/alphaskins.accesswidener"))
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentNeoForge: Configuration = configurations.getByName("developmentNeoForge")

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    developmentNeoForge.extendsFrom(common)
}

dependencies {
    neoForge("net.neoforged:neoforge:21.4.38-beta")

    common(project(":common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":common", "transformProductionNeoForge")) { isTransitive = false }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", stonecutter.current.version)
    inputs.property("neo_version", "21.4.38-beta")
    inputs.property("neo_version_range", "[21.4,)")
    inputs.property("mod_id", rootProject.extra["mod_id"]!!)
    inputs.property("mod_name", rootProject.extra["mod_name"]!!)
    inputs.property("mod_description", rootProject.extra["mod_description"]!!)
    inputs.property("mod_version", rootProject.extra["mod_version"]!!)
    inputs.property("mod_authors", rootProject.extra["mod_authors"]!!)
    inputs.property("mod_license", rootProject.extra["mod_license"]!!)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(
            "version" to project.version,
            "minecraft_version" to stonecutter.current.version,
            "minecraft_version_range" to "[1.21.4,1.22)",
            "neo_version" to "21.4.38-beta",
            "neo_version_range" to "[21.4,)",
            "loader_version_range" to "[2,)",
            "mod_id" to rootProject.extra["mod_id"]!!,
            "mod_name" to rootProject.extra["mod_name"]!!,
            "mod_description" to rootProject.extra["mod_description"]!!,
            "mod_version" to rootProject.extra["mod_version"]!!,
            "mod_authors" to rootProject.extra["mod_authors"]!!,
            "mod_license" to rootProject.extra["mod_license"]!!
        )
    }
}

tasks.shadowJar {
    exclude("architectury.common.json")
    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    injectAccessWidener.set(true)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    dependsOn(tasks.shadowJar)
}

tasks.sourcesJar {
    val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")
    dependsOn(commonSources)
    from(commonSources.archiveFile.map { zipTree(it) })
}

components.java {
    withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
        skip()
    }
}
