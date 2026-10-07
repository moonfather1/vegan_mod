package moonfather.vegan_mod.blocks.transfer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemStackHost
{
    void putItemStack(ItemStack stack, int slot);
    ItemStack getItemStack(int slot);
    void setChanged();
    int getOilVolume();
}
