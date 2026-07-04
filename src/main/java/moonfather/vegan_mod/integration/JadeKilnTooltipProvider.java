package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IBoxElement;
import snownee.jade.api.ui.ScreenDirection;
import snownee.jade.impl.Tooltip;
import snownee.jade.impl.ui.BoxElement;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.impl.ui.SpacerElement;

import java.util.HashMap;

public class JadeKilnTooltipProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor>
{
    private static final JadeKilnTooltipProvider instance = new JadeKilnTooltipProvider();
    public static JadeKilnTooltipProvider getInstance() { return instance; }


    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig)
    {
        BlockEntity be = blockAccessor.getBlockEntity();
        if (be == null)
        {
            be = blockAccessor.getLevel().getBlockEntity(blockAccessor.getPosition().below());
        }
        if (be instanceof KilnBlockEntity kiln)
        {
            int oilVolume;
            if (blockAccessor.getServerData().contains("oil_volume"))
            {
                oilVolume = blockAccessor.getServerData().getInt("oil_volume");
            }
            else
            {
                oilVolume = kiln.getOilVolume();
            }
            if (kiln.getOutput().isEmpty() && kiln.getByproduct().isEmpty() && oilVolume < 250)
            {
                tooltip.add(new SpacerElement(new Vec2(8, 4)));
                tooltip.add(new SpacerElement(new Vec2(8, 4)));
                tooltip.append(MESSAGE_EMPTY);
                tooltip.add(new SpacerElement(new Vec2(8, 4)));
                return;
            }
            Tooltip inner = new Tooltip();
            inner.add(MESSAGE_CONTENT);
            IBoxElement box = new BoxElement(inner, BoxStyle.getTransparent());
            box.setPadding(ScreenDirection.UP, 6);
            box.setPadding(ScreenDirection.DOWN, 6);
            tooltip.add(box);
            if (! kiln.getOutput().isEmpty())
            {
                tooltip.append(ItemStackElement.of(kiln.getOutput()));
            }
            if (! kiln.getByproduct().isEmpty())
            {
                tooltip.append(ItemStackElement.of(kiln.getByproduct()));
            }
            if (oilVolume >= 250)
            {
                int count = oilVolume / 250;
                if (! thick_oil.containsKey(count))
                {
                    thick_oil.put(count, new ItemStack(VeganMod.Items.THICK_OIL.get(), count));
                }
                tooltip.append(ItemStackElement.of(thick_oil.get(count)));
            }
        }
    }
    private static final Component MESSAGE_EMPTY = Component.translatable("gui.jade.plugin_vegan_mod.msg_empty");
    private static final Component MESSAGE_CONTENT = Component.translatable("gui.jade.plugin_vegan_mod.msg_something");
    private static final HashMap<Integer, ItemStack> thick_oil = new HashMap<>();

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor blockAccessor)
    {
        int oil = ((KilnBlockEntity)blockAccessor.getBlockEntity()).getOilVolume();
        data.putInt("oil_volume", oil);
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //

    @Override
    public ResourceLocation getUid()
    {
        return this.pluginId;
    }
    private final ResourceLocation pluginId = ResourceLocation.fromNamespaceAndPath(VeganMod.MODID, "jade_plugin1");
}
