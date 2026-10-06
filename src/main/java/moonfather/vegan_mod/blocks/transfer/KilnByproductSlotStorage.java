package moonfather.vegan_mod.blocks.transfer;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.world.item.Items;


public class KilnByproductSlotStorage extends KilnBaseSlotStorage
{
    public KilnByproductSlotStorage(ItemStackHost host, int slotIndex) { super(host, slotIndex); }



    @Override
    protected boolean canInsert(ItemVariant itemVariant)
    {
        return itemVariant.is(Items.GLASS_BOTTLE) && this.getHostOilAmount() >= 250
                || itemVariant.is(ConventionalItemTags.EMPTY_BUCKETS) && this.getHostOilAmount() >= 1000;
    }

    @Override
    protected boolean canExtract(ItemVariant itemVariant)
    {
        return ! itemVariant.is(Items.GLASS_BOTTLE) && ! itemVariant.is(ConventionalItemTags.EMPTY_BUCKETS);
    }

    @Override
    protected int getCapacity(ItemVariant itemVariant)
    {
        return 1;
    }

    @Override
    public boolean supportsInsertion() { return true; }
    @Override
    public boolean supportsExtraction() { return true; }
}
