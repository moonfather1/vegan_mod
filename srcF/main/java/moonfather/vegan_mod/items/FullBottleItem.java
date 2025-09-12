package moonfather.vegan_mod.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FullBottleItem extends Item
{
    public FullBottleItem()
    {
        super(new Properties());
    }

    @Override
    public boolean hasCraftingRemainingItem()
    {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack)
    {
        return new ItemStack(Items.GLASS_BOTTLE);
    }
}
