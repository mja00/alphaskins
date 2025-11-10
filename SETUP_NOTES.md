# Setup Notes

## Fixed Issues

### Stonecutter Version
- **Issue**: Used non-existent version 0.5.3
- **Fix**: Updated to 0.7.10 (latest stable as of January 2025)
- **Source**: https://plugins.gradle.org/plugin/dev.kikugie.stonecutter

### Stonecutter API Changes in 0.7
- Changed: `stonecutter.current.project.version` → `stonecutter.current.version`
- Changed: `shared { versions ... }` → `create(getRootProject()) { versions ... }`
- Required: `kotlinController = false` for Groovy scripts

### Configuration Syntax
**settings.gradle (Groovy):**
```groovy
stonecutter {
    kotlinController = false
    centralScript = "build.gradle"

    create(getRootProject()) {
        versions "1.20.1", "1.21.1", "1.21.4"
        vcsVersion = "1.21.4"
    }
}
```

## Architecture Decision

### Our Approach vs Template Approach

**Template Approach** (Stonecutter-Arch-Template):
- Creates version-loader combinations as branches
- Example: "1.21.1-fabric", "1.21.1-neoforge", "1.21.4-fabric", etc.
- Requires 6+ branches for our setup (3 versions × 2 loaders)
- Good for projects with version-specific build configurations

**Our Approach** (Alphaskins):
```
alphaskins/
├── common/              # ← Architectury module
├── fabric/              # ← Architectury module
└── neoforge/            # ← Architectury module
    └── Stonecutter versions apply to ALL modules
```

- Separates loaders (Architectury) from versions (Stonecutter)
- Only 3 versions to manage
- Cleaner for simple mods like Alphaskins
- All version-specific code in mixins with `//? if` comments

### Why Our Approach Works Better Here

1. **Simpler Mental Model**: Loaders are directories, versions are conditionals
2. **Less Duplication**: One set of build configs, not per version-loader combo
3. **Easier Maintenance**: Update mixin once, applies to all loaders
4. **Cleaner Git History**: Version switching doesn't create new branches

## Project Structure

```
Root Level:
- settings.gradle     → Defines Stonecutter versions (1.20.1, 1.21.1, 1.21.4)
- build.gradle        → Root config with Architectury + Stonecutter
- common/             → Shared code (mixins with version conditionals)
- fabric/             → Fabric-specific entry point
- neoforge/           → NeoForge-specific entry point

When You Build:
1. Stonecutter preprocesses ALL modules based on active version
2. Architectury compiles common + loader-specific code
3. Output: alphaskins-fabric-5.0.0.jar, alphaskins-neoforge-5.0.0.jar
```

## Version-Specific Code

Stonecutter conditionals work in all three modules (common, fabric, neoforge):

```java
// In common/src/main/java/.../mixin/MixinDownloadingTexture.java
//? if >=1.21.4 {
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
@Mixin(SkinTextureDownloader.class)
public abstract class MixinDownloadingTexture {
//?} else {
/*@Mixin(HttpTexture.class)
public abstract class MixinDownloadingTexture extends SimpleTexture {
    private MixinDownloadingTexture() {super(null);}
*/
//?}
    // Shared mixin code here
}
```

## Testing Locally

```bash
# Clean build
./gradlew clean

# Build all loaders for current version (1.21.4)
./gradlew build

# Build specific loader
./gradlew :fabric:build
./gradlew :neoforge:build

# Switch active version (edit settings.gradle vcsVersion)
# Then re-sync Gradle in IDE
```

## Expected Output

For each Stonecutter version, you'll get:
- `fabric/build/libs/alphaskins-fabric-1.21-5.0.0.jar`
- `neoforge/build/libs/alphaskins-neoforge-1.21-5.0.0.jar`

## Documentation

- Stonecutter Docs: https://stonecutter.kikugie.dev/
- Architectury Docs: https://docs.architectury.dev/
- Template Example: https://github.com/JumperOnJava/Stonecutter-Arch-Template

## Next Steps

1. Run `./gradlew build` locally (should work now with 0.7.10)
2. Test in-game for each version
3. Set up CI/CD for automated builds
4. Configure publishing to Modrinth/CurseForge
