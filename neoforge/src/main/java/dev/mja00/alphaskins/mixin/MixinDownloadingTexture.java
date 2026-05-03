package dev.mja00.alphaskins.mixin;

import com.mojang.blaze3d.platform.NativeImage;
//? if >=1.21.4 {
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
//?} else {
/*import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
*/
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.4 {
@Mixin(SkinTextureDownloader.class)
public abstract class MixinDownloadingTexture {
//?} else {
/*@Mixin(HttpTexture.class)
public abstract class MixinDownloadingTexture extends SimpleTexture {

    private MixinDownloadingTexture() {super(null);}
*/
//?}

    @Inject(method = "setNoAlpha(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }

    @Inject(method = "doNotchTransparencyHack(Lcom/mojang/blaze3d/platform/NativeImage;IIII)V", at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }
//? if <1.21.4 {
/*
}
*/
//?}
}
