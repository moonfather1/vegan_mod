package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class KilnMenu extends AbstractContainerMenu
{
    public KilnMenu(int containerId, Player player, Inventory inventory, KilnBlockEntity kilnBlockEntity, ContainerData dataAccess, ContainerLevelAccess levelAccess, Container blockContainer)
    {
        this(VeganMod.BlockEntities.KILN_MENU_TYPE.get(), containerId, inventory, dataAccess, levelAccess, blockContainer);
    }
    public KilnMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf data)
    {
        this(VeganMod.BlockEntities.KILN_MENU_TYPE.get(), containerId, inv, null, ContainerLevelAccess.NULL, null);
    }
    public KilnMenu(MenuType<KilnMenu> menuType, int containerId, Inventory inventory, ContainerData dataAccess, ContainerLevelAccess levelAccess, Container blockContainer)
    {
        super(menuType, containerId);
        this.access = levelAccess;
        if (blockContainer == null) { blockContainer = new SimpleContainer(8); }
        this.addSlot(new KilnSlots.ResultSlot(inventory.player, blockContainer, SLOT_RESULT, 116, 35, this));
        this.addSlot(new KilnSlots.WoodSlot(blockContainer, SLOT_INPUT1, 56, 17, this)); // input 0
        this.addSlot(new KilnSlots.FuelSlot(blockContainer, SLOT_FUEL, 56, 53, this)); // fuel
        this.addSlot(new KilnSlots.ByproductSlot(blockContainer, SLOT_BYPRODUCT, 152, 35, this)); // byprod
        //---player hotbar slots---
        for (int hor = 0; hor < 9; ++hor)
        {
            this.addSlot(new Slot(inventory, hor, 8 + hor * 18, 142));
        }
        //---player inventory slots---
        for (int ver = 0; ver < 3; ++ver)
        {
            for (int hor = 0; hor < 9; ++hor)
            {
                this.addSlot(new Slot(inventory, hor + ver * 9 + 9, 8 + hor * 18, 84 + ver * 18));
            }
        }
        //---data-slots---
        this.dataSlot0 = this.addDataSlot(dataAccess != null ? DataSlot.forContainer(dataAccess, 0) : DataSlot.standalone());
        this.dataSlot1 = this.addDataSlot(dataAccess != null ? DataSlot.forContainer(dataAccess, 1) : DataSlot.standalone());
        this.dataSlot2 = this.addDataSlot(dataAccess != null ? DataSlot.forContainer(dataAccess, 2) : DataSlot.standalone());
        this.dataSlot3 = this.addDataSlot(dataAccess != null ? DataSlot.forContainer(dataAccess, 3) : DataSlot.standalone());
        //---ready----
        // no longer needed; wired wia container now. // this.access.execute(this::loadFromBE);
        this.dataAccess = dataAccess;
    }

    public static final int SLOT_RESULT = 0;
    public static final int SLOT_INPUT1 = 1;
    public static final int SLOT_FUEL = 2;
    public static final int SLOT_BYPRODUCT = 3;
    public static final int SLOT_HOTBAR_START = SLOT_BYPRODUCT + 1;  //4
    public static final int SLOT_HOTBAR_END = SLOT_HOTBAR_START + 9 - 1;  //12
    public static final int SLOT_INV_START = SLOT_HOTBAR_END + 1;  //13
    public static final int SLOT_INV_END = SLOT_INV_START + 27 - 1;  //39

    private final ContainerLevelAccess access;
    private final ContainerData dataAccess;

    /// ////////////////////////////////////////////////////////////////

    @Override
    public ItemStack quickMoveStack(Player player, int fromIndex)
    {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(fromIndex);
        if (slot == null || ! slot.hasItem())
        {
            return result;
        }
        ItemStack clickedStack = slot.getItem();
        result = clickedStack.copy();
        if (fromIndex == SLOT_RESULT)
        {
            // from kiln output : first inv, then hotbar
            if (! this.moveStack(clickedStack, SLOT_INV_START, SLOT_INV_END, true)
                    && ! this.moveStack(clickedStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, true)
                    && ! this.moveStack(clickedStack, SLOT_INV_START, SLOT_INV_END, false)
                    && ! this.moveStack(clickedStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, false))
            {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(clickedStack, result);
        }
        else if (fromIndex == SLOT_INPUT1 || fromIndex == SLOT_FUEL || fromIndex == SLOT_BYPRODUCT)
        {
            // from kiln: first inv, then hotbar
            if (! this.moveStack(clickedStack, SLOT_INV_START, SLOT_INV_END, true)
                && ! this.moveStack(clickedStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, true)
                && ! this.moveStack(clickedStack, SLOT_INV_START, SLOT_INV_END, false)
                && ! this.moveStack(clickedStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, false))
            {
                return ItemStack.EMPTY;
            }
        }
        else if (fromIndex >= SLOT_INV_START && fromIndex <= SLOT_INV_END)
        {
            // 3 inv rows: first kiln, then hotbar if exists
            if (! this.moveStack(clickedStack, SLOT_INPUT1, SLOT_INPUT1, false)
                    && ! this.moveStack(clickedStack, SLOT_FUEL, SLOT_FUEL, false)
                    && ! this.moveStack(clickedStack, SLOT_HOTBAR_START, SLOT_HOTBAR_END, true))
            {
                return ItemStack.EMPTY;
            }
        }
        else if (fromIndex >= SLOT_HOTBAR_START && fromIndex <= SLOT_HOTBAR_END)
        {
            // hotbar: first kiln, then inv
            if (! this.moveStack(clickedStack, SLOT_INPUT1, SLOT_INPUT1, false)
                    && ! this.moveStack(clickedStack, SLOT_FUEL, SLOT_FUEL, false)
                    && ! this.moveStack(clickedStack, SLOT_INV_START, SLOT_INV_END, false))
            {
                return ItemStack.EMPTY;
            }
        }
        // now cleanup - there are reasons
        if (clickedStack.isEmpty())
        {
            slot.set(ItemStack.EMPTY);
        }
        else
        {
            slot.set(clickedStack); // just setChanged doesn't work for storage drawers
        }
        if (clickedStack.getCount() == result.getCount())
        {
            return ItemStack.EMPTY; // couldn't move anything
        }
        slot.onTake(player, result);
        return result;
    }

    protected boolean moveStack(ItemStack itemStackBeingMoved, int startingSlot, int endingSlot, boolean toOccupiedSlotsOnly)
    {
        boolean result = false;
        int i = startingSlot;
        if (itemStackBeingMoved.isStackable())
        {
            while (! itemStackBeingMoved.isEmpty())
            {
                if (i > endingSlot)
                {
                    break;
                }
                Slot slot = this.slots.get(i);
                ++i;  // don't use i anymore
                if (! slot.isActive()) { continue; }
                ItemStack itemStackInDestination = slot.getItem();
                if (! itemStackInDestination.isEmpty() && ItemStack.isSameItemSameComponents(itemStackBeingMoved, itemStackInDestination))
                {
                    int j = itemStackInDestination.getCount() + itemStackBeingMoved.getCount();
                    int maxSize = slot.getMaxStackSize(itemStackInDestination);
                    if (j <= maxSize)
                    {
                        itemStackBeingMoved.setCount(0);
                        ItemStack copy = itemStackInDestination.copy();
                        copy.setCount(j);
                        slot.set(copy); //slot.setChanged();
                        result = true;
                    }
                    else if (itemStackInDestination.getCount() < maxSize)
                    {
                        itemStackBeingMoved.shrink(maxSize - itemStackInDestination.getCount());
                        ItemStack copy = itemStackInDestination.copy();
                        copy.setCount(maxSize);
                        slot.set(copy); //slot.setChanged();
                        result = true;
                    }
                }
            }
        }
        if (! toOccupiedSlotsOnly)
        {
            i = startingSlot;
            while (! itemStackBeingMoved.isEmpty())
            {
                if (i > endingSlot)
                {
                    break;
                }
                Slot slot = this.slots.get(i);
                ++i;  // don't use i anymore
                if (! slot.isActive()) { continue; }
                if (slot.getItem().isEmpty() && slot.mayPlace(itemStackBeingMoved))
                {
                    int maxSize = slot.getMaxStackSize(itemStackBeingMoved);
                    if (itemStackBeingMoved.getCount() <= maxSize)
                    {
                        slot.set(itemStackBeingMoved.copy()); //slot.setChanged();
                        itemStackBeingMoved.setCount(0);
                        result = true;
                    }
                    else
                    {
                        itemStackBeingMoved.shrink(maxSize);
                        ItemStack copy = itemStackBeingMoved.copy();
                        copy.setCount(maxSize);
                        slot.set(copy); //slot.setChanged();
                        result = true;
                    }
                }
            }
        }
        return result;
    }



    @Override
    public boolean stillValid(Player player)
    {
        return this.access.evaluate((level, blockPos) -> blockPos.distToCenterSqr(player.position()) < 25).orElse(false);
    }



    // works on both sides, though i only need it on client. nicely hooked.
    public int getOilAmount()
    {
        return this.dataSlot3.get();
    }
    public int getProgressPosition()
    {
        return this.dataSlot1.get();
    }
    public int getProgressTarget()
    {
        return this.dataSlot2.get();
    }
    private final DataSlot dataSlot0, dataSlot1, dataSlot2, dataSlot3;

    ////////////////////////////////////////////////////////////////////////

    public void onSlotChanged(int index)
    {
    }

    public void onTakeFromByproductSlot(Player player, ItemStack stack)
    {
        this.access.execute((level, pos) -> this.awardXP(level, pos, player, true));
    }

    public void onTakeFromResultSlot(Player player, ItemStack stack)
    {
        this.access.execute((level, pos) -> this.awardXP(level, pos, player, false));
    }

    private void awardXP(Level level, BlockPos pos, Player player, boolean isByproduct)
    {
        if (level.isClientSide())
        {
            return;
        }
        if (level.getBlockEntity(pos) instanceof KilnBlockEntity kbe)
        {
            kbe.awardXP(player, isByproduct);
        }
    }
}
