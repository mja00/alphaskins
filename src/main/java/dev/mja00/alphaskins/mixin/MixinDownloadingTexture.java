package dev.mja00.alphaskins.mixin;

import net.minecraft.client.renderer.texture.HttpTexture;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HttpTexture.class)
public abstract class MixinDownloadingTexture extends SimpleTexture {

    private MixinDownloadingTexture() {super(null);}

    private static final String STRIP_ALPHA = "Lnet/minecraft/client/renderer/texture/HttpTexture;setNoAlpha(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V";
    private static final String STRIP_COLOR = "Lnet/minecraft/client/renderer/texture/HttpTexture;doNotchTransparencyHack(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V";


    @Inject(method = STRIP_ALPHA, at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }

    @Inject(method = STRIP_COLOR, at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }



}

