package dev.mja00.alphaskins;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("alphaskins")
public class Alphaskins {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public Alphaskins() {
        LOGGER.info("Hello from Alphaskins!");
    }
}
