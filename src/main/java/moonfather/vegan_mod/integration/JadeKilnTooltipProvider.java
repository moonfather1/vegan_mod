package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxElement;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.impl.Tooltip;
import snownee.jade.impl.ui.BoxElementImpl;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.impl.ui.SpacerElement;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class JadeKilnTooltipProvider implements IBlockComponentProvider
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
            Optional<List<Integer>> serverData = JadeKilnDataProvider.getInstance().decodeFromData(blockAccessor);
            if (serverData.isPresent())
            {
                oilVolume = serverData.get().get(0);
            }
            else
            {
                oilVolume = kiln.getOilVolume();
            }

            if (kiln.getOutput().isEmpty() && kiln.getByproduct().isEmpty() && oilVolume < 250)
            {
                tooltip.add(new SpacerElement(8, 4));
                tooltip.add(new SpacerElement(8, 4));
                tooltip.append(MESSAGE_EMPTY);
                tooltip.add(new SpacerElement(8, 4));
                return;
            }
            Tooltip inner = new Tooltip();
            tooltip.add(new SpacerElement(8, 6));
            inner.add(MESSAGE_CONTENT);
            tooltip.add(new SpacerElement(8, 6));
            BoxElement box = new BoxElementImpl(inner, BoxStyle.transparent());
            //box.setPadding(ScreenDirection.UP, 6);
            //box.setPadding(ScreenDirection.DOWN, 6);
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

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //

    @Override
    public Identifier getUid()
    {
        return this.pluginId;
    }
    private final Identifier pluginId = Identifier.fromNamespaceAndPath(VeganMod.MODID, "jade_plugin1");
}