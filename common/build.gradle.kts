import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

extensions.configure<com.github.jengelman.gradle.plugins.shadow.ShadowExtension> {
    // Architectury configuration
}

architectury {
    common("fabric", "neoforge")
}

extensions.configure<LoomGradleExtensionAPI> {
    accessWidenerPath.set(file("src/main/resources/alphaskins.accesswidener"))
}

dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations
    // Do NOT use other classes from fabric loader
    "modImplementation"("net.fabricmc:fabric-loader:0.16.9")
}
