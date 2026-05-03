package dev.mja00.alphaskins.fabric;

import dev.mja00.alphaskins.Alphaskins;
import net.fabricmc.api.ClientModInitializer;

public class AlphaskinsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Alphaskins.init();
    }
}
