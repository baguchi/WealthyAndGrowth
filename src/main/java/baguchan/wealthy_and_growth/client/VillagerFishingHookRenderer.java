package baguchan.wealthy_and_growth.client;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import baguchan.wealthy_and_growth.entity.VillagerFishingHook;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VillagerFishingHookRenderer extends EntityRenderer<VillagerFishingHook, FishingHookRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(WealthyAndGrowth.MODID,"textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);
    private static final double VIEW_BOBBING_SCALE = 960.0D;

    public VillagerFishingHookRenderer(EntityRendererProvider.Context p_174117_) {
        super(p_174117_);
    }

    @Override
    public boolean shouldRender(VillagerFishingHook p_363069_, Frustum p_362635_, double p_361840_, double p_361502_, double p_360380_) {
        return super.shouldRender(p_363069_, p_362635_, p_361840_, p_361502_, p_360380_) && p_363069_.getOwner() != null;
    }

    @Override
    public void render(FishingHookRenderState p_362456_, PoseStack p_114699_, MultiBufferSource p_114700_, int p_114701_) {
        p_114699_.pushPose();
        p_114699_.pushPose();
        p_114699_.scale(0.5F, 0.5F, 0.5F);
        p_114699_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose posestack$pose = p_114699_.last();
        VertexConsumer vertexconsumer = p_114700_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, posestack$pose, p_114701_, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, posestack$pose, p_114701_, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, posestack$pose, p_114701_, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, posestack$pose, p_114701_, 0.0F, 1, 0, 0);
        p_114699_.popPose();
        float f = (float) p_362456_.lineOriginOffset.x;
        float f1 = (float) p_362456_.lineOriginOffset.y;
        float f2 = (float) p_362456_.lineOriginOffset.z;
        VertexConsumer vertexconsumer1 = p_114700_.getBuffer(RenderType.lineStrip());
        PoseStack.Pose posestack$pose1 = p_114699_.last();
        int i = 16;

        for (int j = 0; j <= 16; j++) {
            stringVertex(f, f1, f2, vertexconsumer1, posestack$pose1, fraction(j, 16), fraction(j + 1, 16));
        }

        p_114699_.popPose();
        super.render(p_362456_, p_114699_, p_114700_, p_114701_);
    }

    public static HumanoidArm getHoldingArm(Player p_386900_) {
        return p_386900_.getMainHandItem().canPerformAction(net.neoforged.neoforge.common.ItemAbilities.FISHING_ROD_CAST) ? p_386900_.getMainArm() : p_386900_.getMainArm().getOpposite();
    }

    private Vec3 getPlayerHandPos(LivingEntity entity, float p_341261_) {
        float f = Mth.lerp(p_341261_, entity.yBodyRotO, entity.yBodyRot) * (float) (Math.PI / 180.0);
        double d0 = (double) Mth.sin(f);
        double d1 = (double) Mth.cos(f);
        float f1 = entity.getScale();
        double d2 = (double) 0 * 0.35 * (double) f1;
        double d3 = 0.8 * (double) f1;
        float f2 = entity.isCrouching() ? -0.1875F : 0.0F;
        return entity.getEyePosition(p_341261_).add(-d1 * d2 - d0 * d3, (double) f2 - 0.45 * (double) f1, -d0 * d2 + d1 * d3);

    }

    private static float fraction(int p_114691_, int p_114692_) {
        return (float) p_114691_ / (float) p_114692_;
    }

    private static void vertex(VertexConsumer p_254464_, PoseStack.Pose p_323724_, int p_254296_, float p_253632_, int p_254132_, int p_254171_, int p_254026_) {
        p_254464_.addVertex(p_323724_, p_253632_ - 0.5F, (float) p_254132_ - 0.5F, 0.0F)
                .setColor(-1)
                .setUv((float) p_254171_, (float) p_254026_)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(p_254296_)
                .setNormal(p_323724_, 0.0F, 1.0F, 0.0F);
    }

    private static void stringVertex(
            float p_174119_, float p_174120_, float p_174121_, VertexConsumer p_174122_, PoseStack.Pose p_174123_, float p_174124_, float p_174125_
    ) {
        float f = p_174119_ * p_174124_;
        float f1 = p_174120_ * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = p_174121_ * p_174124_;
        float f3 = p_174119_ * p_174125_ - f;
        float f4 = p_174120_ * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = p_174121_ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        p_174122_.addVertex(p_174123_, f, f1, f2).setColor(-16777216).setNormal(p_174123_, f3, f4, f5);
    }

    @Override
    public FishingHookRenderState createRenderState() {
        return new FishingHookRenderState();
    }

    @Override
    public void extractRenderState(VillagerFishingHook p_361584_, FishingHookRenderState p_364824_, float p_360891_) {
        super.extractRenderState(p_361584_, p_364824_, p_360891_);
        Entity player = p_361584_.getOwner();
        if (player instanceof LivingEntity living) {
            Vec3 vec3 = this.getPlayerHandPos(living, p_360891_);
            Vec3 vec31 = p_361584_.getPosition(p_360891_).add(0.0, 0.25, 0.0);
            p_364824_.lineOriginOffset = vec3.subtract(vec31);
        } else {
            p_364824_.lineOriginOffset = Vec3.ZERO;
        }
    }

    protected boolean affectedByCulling(VillagerFishingHook p_365042_) {
        return false;
    }
}
