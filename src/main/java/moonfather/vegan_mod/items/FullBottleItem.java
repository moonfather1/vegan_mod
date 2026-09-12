package moonfather.vegan_mod.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class FullBottleItem extends IdentifiableItem
{
    public FullBottleItem(String shortId)
    {
        super(shortId);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack)
    {
        return new ItemStackTemplate(Items.GLASS_BOTTLE);
    }
}
