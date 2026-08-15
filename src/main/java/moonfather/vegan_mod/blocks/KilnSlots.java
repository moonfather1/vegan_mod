package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.integration.ImmersiveEngineeringHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.common.Tags;

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
            if (ImmersiveEngineeringHelper.loaded() && stack.is(Items.COAL))
            {
                return true;
            }
            return false;
        }
    }

    public static class FuelSlot extends BaseSlot
    {
        public FuelSlot(Player player, Container container, int index, int x, int y, KilnMenu host)
        {
            super(container, index, x, y, host);
            this.stupidDesignDecision = player.level().fuelValues();
        }
        private final FuelValues stupidDesignDecision;

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return stack.getBurnTime(null, stupidDesignDecision) > 0 && ! stack.is(Tags.Items.BUCKETS);
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
                    || stack.is(Tags.Items.BUCKETS_EMPTY) && oilAmount >= 1000;
        }

        @Override
        public int getMaxStackSize(ItemStack stack)
        {
            return stack.is(Items.DYE.black()) ? 8 : 1;
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
