package moonfather.vegan_mod.blocks.transfer;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class KilnBaseSlotStorage extends SingleStackStorage
{
    private static final int SLOT_COUNT = 4;
    private final ItemStackHost host;
    private final int slotIndex;

    public KilnBaseSlotStorage(ItemStackHost host, int slotIndex)
    {
        this.host = host;
        this.slotIndex = slotIndex;
    }

    protected Level level() { return this.host.getLevel(); } // for fuel values

    protected int getHostOilAmount() { return this.host.getOilVolume();}


    @Override
    protected ItemStack getStack()
    {
        if (slotIndex < 0 || slotIndex >= SLOT_COUNT) { return null; }
        return host.getItemStack(slotIndex);
    }

    @Override
    protected void setStack(ItemStack stack)
    {
        if (slotIndex < 0 || slotIndex >= SLOT_COUNT) { return; }
        host.putItemStack(stack, slotIndex);
    }

    @Override
    protected void onFinalCommit()
    {
        super.onFinalCommit();
        this.host.setChanged();
    }

    @Override
    public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction)
    {
        if (! supportsInsertion() || ! canInsert(insertedVariant)) { return 0; }
        return super.insert(insertedVariant, maxAmount, transaction);
    }  // shouldn't be needed but combined storage object does not check either may() or supports()

    @Override
    public long extract(ItemVariant variant, long maxAmount, TransactionContext transaction)
    {
        if (! supportsExtraction() || ! canExtract(variant)) { return 0; }
        return super.extract(variant, maxAmount, transaction);
    }  // shouldn't be needed but combined storage object does not check either may() or supports()

    @Override
    public abstract boolean supportsInsertion();
    @Override
    public abstract boolean supportsExtraction();
}
