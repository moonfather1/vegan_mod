package moonfather.vegan_mod.blocks.transfer;

public class KilnResultSlotStorage extends KilnBaseSlotStorage
{
    public KilnResultSlotStorage(ItemStackHost host, int slotIndex) { super(host, slotIndex); }



    @Override
    public boolean supportsInsertion() { return false; }
    @Override
    public boolean supportsExtraction() { return true; }
}
