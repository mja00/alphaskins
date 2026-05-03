package dev.mja00.alphaskins.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
//? if >=1.21.4 {
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//?}
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
//? if <1.21.4 {
/*import org.spongepowered.asm.mixin.Shadow;
*/
//?}
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
//? if >=1.21.4 {
public class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {

    public MixinPlayerRenderer(EntityRendererProvider.Context p_174289_, PlayerModel p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }

    @Inject(at = @At("HEAD"), method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/geom/ModelPart;Z)V", cancellable = true)
    private void renderHand(PoseStack p_117776_, MultiBufferSource p_117777_, int p_117778_, ResourceLocation p_360319_, ModelPart p_117780_, boolean p_366655_, CallbackInfo ci) {
        PlayerModel playermodel = this.getModel();
        p_117780_.resetPose();
        p_117780_.visible = true;
        playermodel.leftSleeve.visible = p_366655_;
        playermodel.rightSleeve.visible = p_366655_;
        playermodel.leftArm.zRot = -0.1F;
        playermodel.rightArm.zRot = 0.1F;
        p_117780_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(p_360319_)), p_117778_, OverlayTexture.NO_OVERLAY);
        ci.cancel();
    }

    @Override
    public ResourceLocation getTextureLocation(PlayerRenderState p_368654_) {
        return null;
    }

    @Override
    public PlayerRenderState createRenderState() {
        return null;
    }
}
//?} else {
/*public class MixinPlayerRenderer {

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
        */
        //? if >=1.21.1 {
        /*ResourceLocation resourcelocation = p_117779_.getSkin().texture();
        */
        //?} else {
        /*ResourceLocation resourcelocation = p_117779_.getSkinTextureLocation();
        */
        //?}
        /*p_117780_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(resourcelocation)), p_117778_, OverlayTexture.NO_OVERLAY);
        p_117781_.xRot = 0.0F;
        p_117781_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(resourcelocation)), p_117778_, OverlayTexture.NO_OVERLAY);
        ci.cancel();
    }
}
*/
//?}
