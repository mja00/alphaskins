plugins {
    id("dev.kikugie.stonecutter")
    id("dev.architectury.loom") version "1.14.473" apply false
}

stonecutter active "1.21.4" /* [SC] DO NOT EDIT */

allprojects {
    group = "${rootProject.findProperty("mod.group")}"
    version = "${rootProject.findProperty("mod.version")}"
}
