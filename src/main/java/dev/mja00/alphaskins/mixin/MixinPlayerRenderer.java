package dev.mja00.alphaskins.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {

    @Shadow
    private void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {

    }

    // Yes this method is basically the same but this is better than
    // trying to overwrite incase someone else does some funkiness
    @Inject(at = @At("HEAD"), method = "renderItem(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;)V")
    private void renderItem(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, AbstractClientPlayerEntity playerIn, ModelRenderer rendererArmIn, ModelRenderer rendererArmwearIn, CallbackInfo ci) {

        PlayerRenderer render = (PlayerRenderer) (Object) this;

        PlayerModel<AbstractClientPlayerEntity> playermodel = render.getEntityModel();
        this.setModelVisibilities(playerIn);
        playermodel.swingProgress = 0.0F;
        playermodel.isSneak = false;
        playermodel.swimAnimation = 0.0F;
        playermodel.setRotationAngles(playerIn, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        rendererArmIn.rotateAngleX = 0.0F;
        rendererArmIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(playerIn.getLocationSkin())), combinedLightIn, OverlayTexture.NO_OVERLAY);
        rendererArmwearIn.rotateAngleX = 0.0F;
        rendererArmwearIn.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(playerIn.getLocationSkin())), combinedLightIn, OverlayTexture.NO_OVERLAY);
        ci.cancel();
    }
}
