package moonfather.vegan_mod.blocks.transfer;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.tags.ItemTags;


public class KilnWoodSlotStorage extends KilnBaseSlotStorage
{
    public KilnWoodSlotStorage(ItemStackHost host, int slotIndex) { super(host, slotIndex); }



    @Override
    protected boolean canInsert(ItemVariant itemVariant)
    {
        return itemVariant.is(ItemTags.LOGS_THAT_BURN);
    }

    @Override
    public boolean supportsInsertion() { return true; }
    @Override
    public boolean supportsExtraction() { return false; }
}
