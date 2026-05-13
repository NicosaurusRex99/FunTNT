package nicusha.tnt.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import nicusha.tnt.entities.PaintTntEntity;

public class PaintTntEntityRenderer extends EntityRenderer<PaintTntEntity, TntRenderState> {
    private final BlockModelResolver blockModelResolver;

    public PaintTntEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockModelResolver = context.getBlockModelResolver();
    }

    @Override
    public TntRenderState createRenderState() {
        return new TntRenderState();
    }

    @Override
    public void extractRenderState(PaintTntEntity entity, TntRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.fuseRemainingInTicks = (float)entity.getFuse() - partialTicks + 1.0F;
        this.blockModelResolver.update(state.blockState, entity.getStoredBlockState(), TntRenderer.BLOCK_DISPLAY_CONTEXT);
    }

    @Override
    public void submit(TntRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        float fuse = state.fuseRemainingInTicks;
        if (fuse < 10.0F) {
            float scale = 1.0F + Mth.clamp(1.0F - fuse / 10.0F, 0.0F, 1.0F) * 0.3F;
            poseStack.scale(scale, scale, scale);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        if (!state.blockState.isEmpty()) {
            TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, collector, state.lightCoords, (int)fuse / 5 % 2 == 0, state.outlineColor);
        }
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}