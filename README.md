# Alphaskins

Enable transparency in Minecraft player skins.

## Monorepo Structure

This project uses **Architectury** for multi-loader support and **Stonecutter** for multi-version support, allowing all Minecraft versions and mod loaders to be developed in a single repository.

```
alphaskins/
├── common/              # Shared code across all loaders
│   └── src/main/
│       ├── java/       # Common mixins and logic
│       └── resources/  # Mixin configurations
├── fabric/             # Fabric-specific implementation
│   └── src/main/
│       ├── java/       # Fabric entry point
│       └── resources/  # fabric.mod.json
├── neoforge/           # NeoForge-specific implementation
│   └── src/main/
│       ├── java/       # NeoForge entry point
│       └── resources/  # neoforge.mods.toml
├── build.gradle        # Root build configuration
└── settings.gradle     # Multi-version configuration
```

## Supported Versions

- **Minecraft**: 1.20.1, 1.21.1, 1.21.4
- **Mod Loaders**: Fabric, NeoForge

## Building

### Build all versions and loaders
```bash
./gradlew build
```

### Build specific loader
```bash
./gradlew :fabric:build    # Fabric only
./gradlew :neoforge:build  # NeoForge only
```

### Switch active version
Stonecutter automatically manages version switching. The current active version is `1.21.4` (configured in `settings.gradle`).

## Development

### Project Structure
- **common/**: Contains all the mixin code that's shared across loaders
  - Uses Stonecutter comments `//? if >=version` for version-specific code
- **fabric/**: Fabric-specific initialization and metadata
- **neoforge/**: NeoForge-specific initialization and metadata

### Version-Specific Code

Stonecutter preprocessor comments are used for version-specific code:

```java
//? if >=1.21.4 {
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
//?} else {
/*import net.minecraft.client.renderer.texture.HttpTexture;*/
//?}
```

### How It Works

1. **Architectury** provides:
   - Multi-loader abstraction (Fabric + NeoForge)
   - Common/platform-specific code separation
   - Unified build system

2. **Stonecutter** provides:
   - Multi-version support via conditional compilation
   - Semantic version comparisons (`>=1.21`, `<1.20.5`)
   - Fast version switching without remapping

## Technical Details

### Mixins
- `MixinDownloadingTexture`: Cancels alpha channel stripping in skin textures
- `MixinPlayerRenderer`: Enables translucent rendering for player hands

### Dependencies
- Java 21
- Architectury Loom 1.7-SNAPSHOT
- Architectury Plugin 3.4-SNAPSHOT
- Stonecutter 0.5.3

## License

GPL-3.0

## Author

mja00
