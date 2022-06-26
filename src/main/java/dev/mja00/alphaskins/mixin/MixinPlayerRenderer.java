package dev.mja00.alphaskins.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
    private void renderHand(PoseStack p_117776_, MultiBufferSource p_117777_, int p_117778_, AbstractClientPlayer p_117779_, ModelPart p_117780_, ModelPart p_117781_, CallbackInfo ci) {

        PlayerRenderer render = (PlayerRenderer) (Object) this;

        PlayerModel<AbstractClientPlayer> playermodel = render.getModel();
        this.setModelProperties(p_117779_);
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(p_117779_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        p_117780_.xRot = 0.0F;
        p_117780_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(p_117779_.getSkinTextureLocation())), p_117778_, OverlayTexture.NO_OVERLAY);
        p_117781_.xRot = 0.0F;
        p_117781_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(p_117779_.getSkinTextureLocation())), p_117778_, OverlayTexture.NO_OVERLAY);
        ci.cancel();
    }
}
