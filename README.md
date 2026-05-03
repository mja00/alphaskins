# Alphaskins

Enable transparency in Minecraft player skins.

By default, Minecraft strips the alpha channel from player skin textures. This
mod cancels the strip so transparent pixels render as transparent on both the
player model and the held-hand view.

## Repo layout

Multi-loader, multi-version monorepo using
[Stonecutter](https://github.com/stonecutter-versioning/stonecutter) for
per-Minecraft-version preprocessing.

```
alphaskins/
├── fabric/                       # Fabric loader
│   ├── build.gradle.kts          # Loom + Fabric deps
│   ├── gradle.properties         # loom.platform=fabric
│   ├── src/main/                 # Fabric entrypoint, mixins, fabric.mod.json
│   └── versions/<mc>/            # Per-MC dep pins (fabric_loader, fabric_api)
└── neoforge/                     # NeoForge loader
    ├── build.gradle.kts          # Loom + NeoForge deps
    ├── gradle.properties         # loom.platform=neoforge
    ├── src/main/                 # NeoForge entrypoint, mixins, neoforge.mods.toml
    └── versions/<mc>/            # Per-MC dep pins (neoforge, version ranges)
```

Mixins are duplicated per loader (rather than shared) — the mod is small (~100
LoC across two mixins) so duplication is cheaper than the cross-project
shared-library plumbing that would otherwise be required.

## Building

Active version is set in `stonecutter.gradle.kts`. Build a single target:

```sh
./gradlew :fabric:1.21.4:build
./gradlew :neoforge:1.21.4:build
```

Output jars land in `<loader>/versions/<mc>/build/libs/`.

To build everything and stage the upload-ready jars in one place:

```sh
./gradlew collectJars
# → build/dist/alphaskins-{fabric,neoforge}-<mod>+<mc>-<loader>.jar
```

## Tooling

- Gradle 9.5
- Java 21 (1.21.x) / Java 25 (26.1+)
- Loom plugin per (loader, mc) variant:
  - 1.21.x: [dev.architectury.loom](https://github.com/architectury/architectury-loom) 1.14.473
  - 26.1+ NeoForge: dev.architectury.loom-no-remap 1.14.473
  - 26.1+ Fabric: net.fabricmc.fabric-loom-no-remap 1.14.0-alpha.31 (FabricMC's
    own no-mappings flow for unobfuscated MC)
- Stonecutter 0.7.x

## License

GPL-3.0
