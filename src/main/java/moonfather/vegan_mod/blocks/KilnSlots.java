package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.integration.ImmersiveEngineeringHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemResource;

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

        public static boolean isValidItem(ItemStack stack)
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
        public static boolean isValidItem(ItemResource resource)
        {
            return isValidItem(resource.toStack(1));
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return isValidItem(stack);
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

        public static boolean isValidItem(ItemStack stack, Level moronicDesignDecision)
        {
            if (moronicDesignDecision == null) return false;
            return isValidItem(stack, moronicDesignDecision.fuelValues());
        }
        public static boolean isValidItem(ItemResource resource, Level moronicDesignDecision)
        {
            if (moronicDesignDecision == null) return false;
            return isValidItem(resource.toStack(1), moronicDesignDecision.fuelValues());
        }
        private static boolean isValidItem(ItemStack stack, FuelValues stupidDesignDecision)
        {
            return stack.getBurnTime(null, stupidDesignDecision) > 0 && ! stack.is(Tags.Items.BUCKETS);
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return isValidItem(stack, stupidDesignDecision);
        }
    }



    public static class ByproductSlot extends BaseSlot
    {
        public ByproductSlot(Container container, int index, int x, int y, KilnMenu host) { super(container, index, x, y, host); }

        public static boolean isValidItem(ItemStack stack, int oilAmount)
        {
            return stack.is(Items.GLASS_BOTTLE) && oilAmount >= 250
                    || stack.is(Tags.Items.BUCKETS_EMPTY) && oilAmount >= 1000;
        }
        public static boolean isValidItem(ItemResource resource, int oilAmount)
        {
            return isValidItem(resource.toStack(1), oilAmount);
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            int oilAmount = this.host.getOilAmount();
            return isValidItem(stack, oilAmount);
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
