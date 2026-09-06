package moonfather.vegan_mod.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity>
{
    public DryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) { }


    @Override
    public void render(DryingRackBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
    {
        ItemStack itemStack = blockEntity.getItem();
        if (itemStack.isEmpty())
        {
            return;
        }
        if (itemRenderer == null)
        {
            itemRenderer = Minecraft.getInstance().getItemRenderer();
        }
        poseStack.pushPose();
        poseStack.translate(0, 10.1/16f, 0);   // on top
        poseStack.translate(0.5D, 0, 0.5D); // center
        poseStack.mulPose(XPlus90); // lay items horizontal
        poseStack.mulPose(ZPlus180); // lay items horizontal
        float itemScale = 0.60f;
        poseStack.scale(itemScale, itemScale, itemScale / 1.03f); // last part flattens them a little. i don't know how else to deal with blocks
        if (is3d(itemStack, blockEntity.getLevel(), packedLight))
        {
            poseStack.translate(0, 0, -3.75/16d); // center
        }
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, null, blockEntity.getBlockPos().hashCode());
        poseStack.popPose();
    }
    private static final Quaternionf XPlus90 = new Quaternionf().fromAxisAngleDeg(1, 0, 0, 90);
    private static final Quaternionf ZPlus180 = new Quaternionf().fromAxisAngleDeg(0, 0, 1, 180);

    ///////////////////////////////////////////

    private static final Map<Item, Boolean> mapFor3d = new HashMap<>();
    private static ItemRenderer itemRenderer = null;

    private static boolean is3d(ItemStack itemStack, Level level, int packedLight)
    {
        Boolean cached = mapFor3d.getOrDefault(itemStack.getItem(), null);
        if (cached != null)
        {
            return cached;
        }
        boolean result = itemStack.getItem() instanceof BlockItem && Minecraft.getInstance().getItemRenderer().getModel(itemStack, level, null, packedLight).isGui3d();
        mapFor3d.put(itemStack.getItem(), result);
        return result;
    }
}
