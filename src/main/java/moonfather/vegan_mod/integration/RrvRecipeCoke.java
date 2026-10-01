package moonfather.vegan_mod.integration;

import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class RrvRecipeCoke extends RrvRecipeCharcoal
{
    public RrvRecipeCoke()
    {
        super();
        this.in1 = SlotContent.of(Items.COAL);
        this.out1 = SlotContent.of(ImmersiveEngineeringHelper.getCoalItem());
        this.out3 = SlotContent.of();
    }

    @Override
    public Identifier getId() { return Identifier.fromNamespaceAndPath(VeganMod.MODID, "/kiln2"); }

    @Override
    public List<SlotContent> getResults()
    {
        return List.of(out1, out2);
    }
}
