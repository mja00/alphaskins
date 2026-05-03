package dev.mja00.alphaskins;

import dev.architectury.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Alphaskins {
    public static final String MOD_ID = "alphaskins";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Hello from Alphaskins on {}!", Platform.getName());
    }
}
