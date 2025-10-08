package baguchan.wealthy_and_growth.client;

import baguchan.wealthy_and_growth.entity.VillagerFishingHook;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VillagerFishingHookRenderer extends EntityRenderer<VillagerFishingHook, FishingHookRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/fishing_hook.png");
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
    public void submit(FishingHookRenderState p_451173_, PoseStack p_434862_, SubmitNodeCollector p_433298_, CameraRenderState p_451318_) {
        p_434862_.pushPose();
        p_434862_.pushPose();
        p_434862_.scale(0.5F, 0.5F, 0.5F);
        p_434862_.mulPose(p_451318_.orientation);
        p_433298_.submitCustomGeometry(p_434862_, RENDER_TYPE, (p_434943_, p_432878_) -> {
            vertex(p_432878_, p_434943_, p_451173_.lightCoords, 0.0F, 0, 0, 1);
            vertex(p_432878_, p_434943_, p_451173_.lightCoords, 1.0F, 0, 1, 1);
            vertex(p_432878_, p_434943_, p_451173_.lightCoords, 1.0F, 1, 1, 0);
            vertex(p_432878_, p_434943_, p_451173_.lightCoords, 0.0F, 1, 0, 0);
        });
        p_434862_.popPose();
        float f = (float) p_451173_.lineOriginOffset.x;
        float f1 = (float) p_451173_.lineOriginOffset.y;
        float f2 = (float) p_451173_.lineOriginOffset.z;
        p_433298_.submitCustomGeometry(p_434862_, RenderType.lines(), (p_436505_, p_436506_) -> {
            int i = 16;

            for (int j = 0; j < 16; j++) {
                float f3 = fraction(j, 16);
                float f4 = fraction(j + 1, 16);
                stringVertex(f, f1, f2, p_436506_, p_436505_, f3, f4);
                stringVertex(f, f1, f2, p_436506_, p_436505_, f4, f3);
            }
        });
        p_434862_.popPose();
        super.submit(p_451173_, p_434862_, p_433298_, p_451318_);
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
