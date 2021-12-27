package dev.mja00.alphaskins.mixin;

import com.mojang.realmsclient.util.SkinProcessor;
import net.minecraft.client.renderer.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinProcessor.class)
public class MixinSkinProcessor {

    private static final String STRIP_ALPHA = "Lcom/mojang/realmsclient/util/SkinProcessor;func_225229_b(IIII)V";
    private static final String STRIP_COLOR = "Lcom/mojang/realmsclient/util/SkinProcessor;func_225227_a(IIII)V";


    @Inject(method = STRIP_ALPHA, at = @At("HEAD"), cancellable = true)
    private static void cancelAlphaStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }

    @Inject(method = STRIP_COLOR, at = @At("HEAD"), cancellable = true)
    private static void cancelColorStrip(NativeImage image, int beginX, int beginY, int endX, int endY, CallbackInfo info) {
        info.cancel();
    }
}
