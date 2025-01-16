package dev.mja00.alphaskins.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinTextureDownloader.class)
public abstract class MixinDownloadingTexture {

    @Inject(method = "setNoAlpha(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }

    @Inject(method = "doNotchTransparencyHack(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }
}
