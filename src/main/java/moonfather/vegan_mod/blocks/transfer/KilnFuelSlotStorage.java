package moonfather.vegan_mod.blocks.transfer;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.tags.ItemTags;


public class KilnFuelSlotStorage extends KilnBaseSlotStorage
{
    public KilnFuelSlotStorage(ItemStackHost host, int slotIndex) { super(host, slotIndex); }



    @Override
    protected boolean canInsert(ItemVariant itemVariant)
    {
        if (itemVariant.is(ConventionalItemTags.BUCKETS)) { return false; }
        if (itemVariant.is(ItemTags.LOGS_THAT_BURN)) { return false; }
        return this.level() != null && this.level().fuelValues().isFuel(itemVariant.toStack(1));
    }

    @Override
    public boolean supportsInsertion() { return true; }
    @Override
    public boolean supportsExtraction() { return false; }
}
