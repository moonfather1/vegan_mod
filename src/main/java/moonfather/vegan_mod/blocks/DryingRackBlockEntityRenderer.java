package moonfather.vegan_mod.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity, DryingRackBlockEntityRenderer.SimpleRenderState>
{
    public DryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }
    private final ItemModelResolver itemModelResolver;



    @Override
    public SimpleRenderState createRenderState()
    {
        return new SimpleRenderState();
    }

    @Override
    public void extractRenderState(DryingRackBlockEntity blockEntity, SimpleRenderState renderState, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTicks, cameraPosition, breakProgress);
        // and now fill in our stuff
        int seedBase = Long.valueOf(blockEntity.getBlockPos().asLong()).hashCode();
        renderState.direction = Direction.fromYRot((blockEntity.hashCode() % 4) * 90);
        if (! blockEntity.getItem().isEmpty())
        {
            ItemStackRenderState itemState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemState, blockEntity.getItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seedBase + 1);
            renderState.item = itemState;
            renderState.is3d = is3d(blockEntity.getItem(), null, renderState.lightCoords);
        }
        else
        {
            renderState.item = null;
        }
    }

    @Override
    public void submit(SimpleRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState)
    {
        ItemStackRenderState itemStack = state.item;
        if (itemStack != null)
        {
            //poseStack.pushPose();
            //poseStack.translate(0.5D, 0.44921875D, 0.5D);              //poseStack.translate(0.5D, 0.44921875D, 0.5D);
            //Direction direction = state.direction;
//            float f = -direction.toYRot();
//            poseStack.mulPose(Axis.YP.rotationDegrees(f));
//            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
//            if (direction.getAxis().equals(Direction.Axis.Z))
//            {
//                poseStack.translate(0.0d + direction.getStepZ() * +0.5d, direction.getStepZ() * +0.5D, -10 / 16D - 1 / 64d);   //poseStack.translate(-0.3125D, -0.3125D, 0.0D);     // Z is height    y0.75->0
//            }
//            else
//            {
//                poseStack.translate(direction.getStepX() * -0.5, direction.getStepX() * +0.5, -10 / 16D - 1 / 64d);
//            }
//            poseStack.scale(0.5F, 0.5F, 0.5F);     //was 0.375
//            itemStack.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
//            poseStack.popPose();-----------------------------------------
            poseStack.pushPose();
            poseStack.translate(0, 10.1/16f, 0);   // on top
            poseStack.translate(0.5D, 0, 0.5D); // center
            poseStack.mulPose(XPlus90); // lay items horizontal
            poseStack.mulPose(ZPlus180); // lay items horizontal
            float itemScale = 0.60f;
            poseStack.scale(itemScale, itemScale, itemScale / 1.03f); // last part flattens them a little. i don't know how else to deal with blocks
            if (state.is3d)
            {
                poseStack.translate(0, 0, -3.75/16d); // center
            }
            itemStack.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }



    private static final Quaternionf XPlus90 = new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90);
    private static final Quaternionf ZPlus180 = new Quaternionf().fromAxisAngleDeg(0, 0, 1, 180);

    ///////////////////////////////////////////

    private static final Map<Item, Boolean> mapFor3d = new HashMap<>();

    private static boolean is3d(ItemStack itemStack, Level level, int packedLight)
    {
        Boolean cached = mapFor3d.getOrDefault(itemStack.getItem(), null);
        if (cached != null)
        {
            return cached;
        }
        boolean result = itemStack.getItem() instanceof BlockItem ;// && Minecraft.getInstance().getItemRenderer().getModel(itemStack, level, null, packedLight).isGui3d();
        mapFor3d.put(itemStack.getItem(), result);  // todo fix above
        return result;
    }

    public static class SimpleRenderState extends BlockEntityRenderState
    {
        public ItemStackRenderState item = new ItemStackRenderState();
        public Direction direction = Direction.EAST;
        public boolean is3d = false;
    }
}
