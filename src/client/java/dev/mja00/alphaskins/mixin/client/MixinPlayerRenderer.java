package dev.mja00.alphaskins.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {

    @Shadow
    private void setModelProperties(AbstractClientPlayer clientPlayer) {

    }

    @Inject(at = @At("HEAD"), method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;)V", cancellable = true)
    private void renderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2, CallbackInfo ci) {

        PlayerRenderer render = (PlayerRenderer) (Object) this;

        PlayerModel<AbstractClientPlayer> playermodel = render.getModel();
        this.setModelProperties(abstractClientPlayer);
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(abstractClientPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        modelPart.xRot = 0.0F;
        modelPart.render(poseStack, multiBufferSource.getBuffer(RenderType.entityTranslucent(abstractClientPlayer.getSkinTextureLocation())), i, OverlayTexture.NO_OVERLAY);
        modelPart2.xRot = 0.0F;
        modelPart2.render(poseStack, multiBufferSource.getBuffer(RenderType.entityTranslucent(abstractClientPlayer.getSkinTextureLocation())), i, OverlayTexture.NO_OVERLAY);
        ci.cancel();
    }
}
