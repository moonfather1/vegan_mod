package moonfather.vegan_mod.blocks;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;

public class KilnSlots
{
    private static class BaseSlot extends Slot
    {
        protected final KilnMenu host;

        public BaseSlot(Container container, int index, int x, int y, KilnMenu host)
        {
            super(container, index, x, y);
            this.host = host;
        }

        @Override
        public void setChanged()
        {
            super.setChanged();
            this.host.onSlotChanged(this.index);
        }
    }

    public static class WoodSlot extends BaseSlot
    {
        public WoodSlot(Container container, int index, int x, int y, KilnMenu host) { super(container, index, x, y, host); }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            if (stack.is(ItemTags.LOGS_THAT_BURN))
            {
                return true;
            }
            if (false /*ImmersiveEngineeringHelper.loaded()*/ && stack.is(Items.COAL))
            {
                return true;
            }
            return false;
        }
    }

    public static class FuelSlot extends BaseSlot
    {
        public FuelSlot(Container container, int index, int x, int y, KilnMenu host) { super(container, index, x, y, host); }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            Integer burnTime = FuelRegistry.INSTANCE.get(stack.getItem());
            //Integer fromMap = FurnaceBlockEntity.getFuel().get(stack.getItem());      return fromMap != null ? fromMap : 0;

            return burnTime != null && burnTime > 0 && ! stack.is(ConventionalItemTags.BUCKETS);
        }
    }

    public static class ByproductSlot extends BaseSlot
    {
        public ByproductSlot(Container container, int index, int x, int y, KilnMenu host) { super(container, index, x, y, host); }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            int oilAmount = this.host.getOilAmount();
            return stack.is(Items.GLASS_BOTTLE) && oilAmount >= 250
                    || stack.is(Items.BUCKET) && oilAmount >= 1000;
        }

        @Override
        public int getMaxStackSize(ItemStack stack)
        {
            return stack.is(Items.BLACK_DYE) ? 8 : 1;
        }

        @Override
        public void onTake(Player player, ItemStack stack)
        {
            super.onTake(player, stack);
            this.host.onTakeFromByproductSlot(player, stack);
        }
    }

    public static class ResultSlot extends FurnaceResultSlot
    {
        protected final KilnMenu host;

        public ResultSlot(Player player, Container container, int slot, int xPosition, int yPosition, KilnMenu host)
        {
            super(player, container, slot, xPosition, yPosition);
            this.host = host;
        }

        @Override
        public void onTake(Player player, ItemStack stack)
        {
            super.onTake(player, stack);
            this.host.onTakeFromResultSlot(player, stack);
        }
    }
}
