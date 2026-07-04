package moonfather.vegan_mod.integration;

import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementHorizontal;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlock;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class TOPInfoProvider implements IProbeInfoProvider
{
    @Override
    public ResourceLocation getID()
    {
        return ResourceLocation.fromNamespaceAndPath(VeganMod.MODID, "top_kiln");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData)
    {
        if (blockState.getBlock() instanceof KilnBlock)
        {
            KilnBlockEntity kiln = (KilnBlockEntity) level.getBlockEntity(probeHitData.getPos());
            if  (kiln == null)
            {
                kiln = (KilnBlockEntity) level.getBlockEntity(probeHitData.getPos().below());
                if  (kiln == null)
                {
                    return;
                }
            }
            if (kiln.getOutput().isEmpty() && kiln.getByproduct().isEmpty() && kiln.getOilVolume() < 250)
            {
                probeInfo.text(MESSAGE_EMPTY, (new TextStyle().topPadding(6).bottomPadding(4)));
                return;
            }
            ElementHorizontal row = new ElementHorizontal();
            row.text(MESSAGE_CONTENT, (new TextStyle().topPadding(8)));
            if (! kiln.getOutput().isEmpty())
            {
                row.item(kiln.getOutput());
            }
            if (! kiln.getByproduct().isEmpty())
            {
                row.item(kiln.getByproduct());
            }
            if (kiln.getOilVolume() >= 250)
            {
                int count = kiln.getOilVolume() / 250;
                if (! thick_oil.containsKey(count))
                {
                    thick_oil.put(count, new ItemStack(VeganMod.Items.THICK_OIL.get(), count));
                }
                row.item(thick_oil.get(count));
            }
            probeInfo.element(row);
        }
    }
    private static final Component MESSAGE_EMPTY = Component.translatable("gui.jade.plugin_vegan_mod.msg_empty");
    private static final Component MESSAGE_CONTENT = Component.translatable("gui.jade.plugin_vegan_mod.msg_something");
    private static final HashMap<Integer, ItemStack> thick_oil = new HashMap<>();
}
