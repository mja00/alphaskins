plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.14.473" apply false
}

stonecutter active "1.21.4" /* [SC] DO NOT EDIT */

allprojects {
    group = "${rootProject.findProperty("mod.group")}"
    version = "${rootProject.findProperty("mod.version")}"
}

// Aggregate all built mod jars into build/dist/ for easy upload.
// Skips -sources, -dev, and -dev-shadow classifiers.
// Stonecutter 0.7+ generates per-leaf preprocessed sources, so a leaf's
// plain `build` produces the correct per-version jar — no chiseled task needed.
tasks.register<Copy>("collectJars") {
    group = "distribution"
    description = "Copies every (loader, mc) jar into build/dist/"
    into(layout.buildDirectory.dir("dist"))

    subprojects {
        // Only the leaf (loader, version) projects produce jars.
        if (parent != rootProject) {
            this@register.dependsOn(tasks.named("build"))
            this@register.from(layout.buildDirectory.dir("libs")) {
                include("*.jar")
                exclude("*-sources.jar", "*-dev.jar", "*-dev-shadow.jar")
            }
        }
    }
}
