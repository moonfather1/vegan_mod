package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.integration.ImmersiveEngineeringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KilnBlockEntity extends StandardContainerBlockEntity implements MenuProvider
{
    public KilnBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
        this.setCapacity(container_capacity);
        this.blockContainer = new MostlySimpleContainer(container_capacity, this);
    }
    public KilnBlockEntity(BlockPos blockPos, BlockState blockState) { this(VeganMod.BlockEntities.KILN_BE.get(), blockPos, blockState); }

    /////////////////////////////////////////////////////////////////////

    private int dataStatus, dataBigTicksDone, dataBigTicksTarget, dataOil, dataExcessFuelPaid, dataCurrentFuelPaid, dataXp1, dataXp2, dataInputFlags;

    public static final int BASE_TIME_IN_SECONDS = 600;
    public static final int FUEL_FOR_ONE_OPERATION = 200; // stick is 100  // i wanted double but that makes charcoal smelt as much as planks
    public static final int OIL_GAIN_PER_OPERATION = 50;



    @Override  @NotNull
    public Component getDisplayName()
    {
        return name;
    }
    private static final Component name = Component.translatable("block.vegan_mod.kiln");

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player)
    {
        ContainerLevelAccess levelAccess = this.getLevel() != null ? ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()) : ContainerLevelAccess.NULL;
        return new KilnMenu(i, player, inventory, this, this.dataAccess, levelAccess, this.blockContainer);
    }
    protected final ContainerData dataAccess = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch (index)
            {
                case 0 -> KilnBlockEntity.this.dataStatus;
                case 1 -> KilnBlockEntity.this.dataBigTicksDone;
                case 2 -> KilnBlockEntity.this.dataBigTicksTarget;
                case 3 -> KilnBlockEntity.this.dataOil;
                default -> 987;
            };
        }

        @Override
        public void set(int index, int value)
        {
            switch (index)
            {
                case 0 -> KilnBlockEntity.this.dataStatus = value;
                case 1 -> KilnBlockEntity.this.dataBigTicksDone = value;
                case 2 -> KilnBlockEntity.this.dataBigTicksTarget = value;
                case 3 -> KilnBlockEntity.this.dataOil = value;
                default -> KilnBlockEntity.this.dataStatus = value / (index / 1234);
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };



    public void dropAll()
    {
        this.dropEverything();
    }



    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T blockEntity)
    {
        final int SECONDS_BETWEEN_OUR_TICKS = 2;
        if (level.getLevelData().getGameTime() % (20 * SECONDS_BETWEEN_OUR_TICKS) != 12)
        {
            return;
        }
        if (! (blockEntity instanceof KilnBlockEntity kbe)) { return; }
        ItemStack byproduct = kbe.getContainer().getItem(KilnMenu.SLOT_BYPRODUCT);
        //---------------------------------------------------------//
        if (byproduct.is(Items.GLASS_BOTTLE) && kbe.dataOil >= 250)
        {
            kbe.dataOil -= 250;
            ItemStack oil = VeganMod.Items.THICK_OIL.toStack();
            oil.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.thick_oil2"));
            kbe.getContainer().setItem(KilnMenu.SLOT_BYPRODUCT, oil);
        }
        else if (ImmersiveEngineeringHelper.loaded() && byproduct.is(Tags.Items.BUCKETS_EMPTY) && kbe.dataOil >= 1000)
        {
            kbe.dataOil -= 1000;
            ItemStack oil = ImmersiveEngineeringHelper.getBucketItem().getDefaultInstance();
            kbe.getContainer().setItem(KilnMenu.SLOT_BYPRODUCT, oil);
        }
        //-----------------------------------------------------//
        if (blockState.getValue(KilnBlock.LIT) != (kbe.dataStatus != 0))
        {
            level.setBlock(blockPos, blockState.setValue(KilnBlock.LIT, kbe.dataStatus != 0), 3);
        }
        //-----------------------------------------------------//
        ItemStack fuel = kbe.getContainer().getItem(KilnMenu.SLOT_FUEL);
        ItemStack input1 = kbe.getContainer().getItem(KilnMenu.SLOT_INPUT1);
        if (kbe.dataStatus == 1) // running, less than 1/3 done
        {
            if (input1.isEmpty())
            {
                kbe.dataStatus = 0;  kbe.dataBigTicksDone = 0;
                level.sendBlockUpdated(blockPos, blockState, blockState, 2);
                return;   // fuel charged, no need to check it
            }
            if (kbe.dataBigTicksDone >= kbe.dataBigTicksTarget / 3)
            {
                Item output = getRecipeOutput(input1);
                kbe.dataInputFlags = 0;
                if (input1.is(LOGS_THAT_GIVE_MORE_CHARCOAL)) { kbe.dataInputFlags |= 256; }
                if (input1.is(LOGS_THAT_GIVE_MORE_TAR)) { kbe.dataInputFlags |= 512; }
                int countToProcess = Math.min(kbe.dataCurrentFuelPaid, input1.getCount());
                input1.shrink(countToProcess);
                kbe.getContainer().setItem(7, new ItemStack(output, countToProcess));
                kbe.dataStatus = 2;
                kbe.dataBigTicksDone += 1;
                level.sendBlockUpdated(blockPos, blockState, blockState, 2);
                return;
            }
            kbe.dataBigTicksDone += 1;
        }
        ItemStack result = kbe.getContainer().getItem(KilnMenu.SLOT_RESULT);
        if (kbe.dataStatus == 2) // running, more than 1/3 done
        {
            if (kbe.dataBigTicksDone >= kbe.dataBigTicksTarget)
            {
                int numberOfInputItems = kbe.getStoredItem(7).getCount();
                int oilToStore = OIL_GAIN_PER_OPERATION * numberOfInputItems;
                int resultToStore = resolveCharcoal(kbe.dataInputFlags, kbe.getStoredItem(7), level.getRandom());
                int tarToStore = resolveTar(kbe.dataInputFlags, kbe.getStoredItem(7), level.getRandom());
                if (result.isEmpty())
                {
                    result = kbe.getStoredItem(7).copyWithCount(resultToStore);
                    kbe.clearItem(7);
                    kbe.storeItem(KilnMenu.SLOT_RESULT, result);
                }
                else
                {
                    result.grow(resultToStore);
                    result.limitSize(result.getMaxStackSize());
                    kbe.clearItem(7);
                }
                kbe.dataXp1 += numberOfInputItems;
                kbe.dataOil += oilToStore;
                kbe.dataXp2 += (kbe.dataOil / 5 > (kbe.dataOil - oilToStore) / 5) ? 1 : 0;
                if (Config.kiln_gives_tar_paint())
                {
                    if (byproduct.isEmpty() || (byproduct.is(Tags.Items.DYES_BLACK) && byproduct.getCount() < 4))
                    {
                        if (level.getRandom().nextInt(10) == 3)
                        {
                            if (byproduct.isEmpty())
                            {
                                byproduct = new ItemStack(Items.BLACK_DYE, tarToStore);
                                byproduct.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.black_paint"));
                                kbe.getContainer().setItem(KilnMenu.SLOT_BYPRODUCT, byproduct);
                            }
                            else
                            {
                                byproduct.grow(tarToStore);
                            }
                            kbe.dataXp2 += 1;
                        }
                    }
                }
                kbe.dataStatus = 0;
                kbe.dataBigTicksDone = 0;
                level.sendBlockUpdated(blockPos, blockState, blockState, 2);
            }  // intentionally no return here. i used to have it and it caused a 2sec pause. fine by me, but i know it would throw people off.
            else
            {
                kbe.dataBigTicksDone += 1;
            }
        }
        if (kbe.dataStatus == 0) // idle
        {
            int storingAValueForSimplerReturns = kbe.dataExcessFuelPaid;
            kbe.dataExcessFuelPaid = 0; // so that i can just return;
            if (input1.isEmpty()) { return; }
            int fuelValue = fuel.getBurnTime(null, level.fuelValues()) * fuel.getCount();
            if (fuelValue < FUEL_FOR_ONE_OPERATION && storingAValueForSimplerReturns == 0) { return; }
            if (! result.isEmpty() && result.getCount() == result.getMaxStackSize())  { return; }
            Item output = getRecipeOutput(input1);
            if (! result.isEmpty() && ! result.is(output))  { return; }
            kbe.dataExcessFuelPaid = storingAValueForSimplerReturns;
            // ok then...
            kbe.resolveFuel(fuel, input1.getCount(), level);
            if (kbe.dataCurrentFuelPaid == 0) { return; } //will never hap
            kbe.dataStatus = 1;  // light it up!!
            kbe.dataBigTicksDone = 1;  // actually 0 but this is to show fire in gui. one tick changes nothing (even a big 2sec tick).
            double multiplier = (! input1.is(Items.COAL)) ? 1.0 : 1.5;
            kbe.dataBigTicksTarget = (int) Math.floor(BASE_TIME_IN_SECONDS * multiplier * Config.kiln_time_multiplier() / SECONDS_BETWEEN_OUR_TICKS);
            level.sendBlockUpdated(blockPos, blockState, blockState, 2);
        }
    }

    private static Item getRecipeOutput(ItemStack input)
    {
        return (! input.is(Items.COAL)) ? Items.CHARCOAL : ImmersiveEngineeringHelper.getCoalItem();
    }

    private static int resolveCharcoal(int inputFlags, ItemStack resultItem, RandomSource random)
    {
        int result = resultItem.getCount();
        if (resultItem.is(Items.CHARCOAL))
        {
            if ((inputFlags & 256) != 0) // gives more charcoal
            {
                if (random.nextBoolean())
                {
                    result *= 2;
                }
            }
        }
        return result;
    }

    private static int resolveTar(int inputFlags, ItemStack resultItem, RandomSource random)
    {
        int result = resultItem.getCount();
        if (resultItem.is(Items.CHARCOAL))
        {
            if ((inputFlags & 512) != 0) // gives more tar
            {
                if (random.nextBoolean())
                {
                    result *= 2;
                }
            }
        }
        return result;
    }

    private void resolveFuel(ItemStack fuel, int inputCount, Level level)
    {
        this.dataCurrentFuelPaid = 0;
        if (this.dataExcessFuelPaid > 0)
        {
            int dif = Math.min(this.dataExcessFuelPaid, Config.kiln_multiples_at_once());
            dif = Math.min(dif, inputCount);
            this.dataExcessFuelPaid -= dif;
            this.dataCurrentFuelPaid += dif;
            if (this.dataCurrentFuelPaid == Config.kiln_multiples_at_once() || this.dataCurrentFuelPaid == inputCount)
            {
                return;
            }
            // we know that this.dataCurrentFuelPaid == this.dataExcessFuelPaid.previous, but we can fit more...
            int fuelTime1 = fuel.getBurnTime(null, level.fuelValues());
            if (fuelTime1 <= FUEL_FOR_ONE_OPERATION)
            {
                int fuelNeededForOneLog = (int) Math.ceil(FUEL_FOR_ONE_OPERATION * 1d / fuelTime1);
                while (fuel.getCount() >= fuelNeededForOneLog)
                {
                    fuel.shrink(fuelNeededForOneLog);
                    this.dataCurrentFuelPaid += 1;
                    if (fuel.isEmpty() || this.dataCurrentFuelPaid == Config.kiln_multiples_at_once() || this.dataCurrentFuelPaid == inputCount)
                    {
                        return;
                    }
                }
            }
        }
        else
        {
            // no excess
            int fuelTime1 = fuel.getBurnTime(null, level.fuelValues());
            if (fuelTime1 <= FUEL_FOR_ONE_OPERATION)
            {
                int fuelNeededForOneLog = (int) Math.ceil(FUEL_FOR_ONE_OPERATION * 1d / fuelTime1);
                while (fuel.getCount() >= fuelNeededForOneLog)
                {
                    fuel.shrink(fuelNeededForOneLog);
                    this.dataCurrentFuelPaid += 1;
                    if (fuel.isEmpty() || this.dataCurrentFuelPaid == Config.kiln_multiples_at_once() || this.dataCurrentFuelPaid == inputCount)
                    {
                        return;
                    }
                }
            }
            else
            {
                int take = 1;  double value = fuelTime1 * 1.0f / FUEL_FOR_ONE_OPERATION;
                if ((value == 1.5 || value == 2.5) && fuel.getCount() > 1) { take = 2;}
                int inputCovered = (int) Math.floor(value * take);
                int max = Math.min(Config.kiln_multiples_at_once(), inputCount);
                int willSmelt = Math.min(inputCovered, max);
                this.dataCurrentFuelPaid = willSmelt;
                this.dataExcessFuelPaid = inputCovered - willSmelt;
                fuel.shrink(take);
            }
        }
    }

    private static final TagKey<Item> LOGS_THAT_GIVE_MORE_CHARCOAL = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, "logs_that_give_more_charcoal"));
    private static final TagKey<Item> LOGS_THAT_GIVE_MORE_TAR = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, "logs_that_give_more_tar"));

    /////////////////////////////////////////////////////

    private static final int container_capacity = 8;
    private final Container blockContainer;
    @Override
    protected Container getContainer() { return this.blockContainer; }

    ///////////////////////////////////////////////////

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        this.dropAll();
    }

    ///////////////////////////////////////////

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.dataInputFlags = input.getIntOr("kiln_data8", 0);
        this.dataCurrentFuelPaid = input.getIntOr("kiln_data7", 0);
        this.dataXp2 = input.getIntOr("kiln_data6", 0);
        this.dataXp1 = input.getIntOr("kiln_data5", 0);
        this.dataExcessFuelPaid = input.getIntOr("kiln_data4", 0);
        this.dataOil = input.getIntOr("kiln_data3", 0);
        this.dataBigTicksTarget = input.getIntOr("kiln_data2", 0);
        this.dataBigTicksDone = input.getIntOr("kiln_data1", 0);
        this.dataStatus = input.getIntOr("kiln_data0", 0);
    }

    @Override
    protected void saveInternal(ValueOutput output)
    {
        output.putInt("kiln_data8", this.dataInputFlags);
        output.putInt("kiln_data7", this.dataCurrentFuelPaid);
        output.putInt("kiln_data6", this.dataXp1);
        output.putInt("kiln_data5", this.dataXp1);
        output.putInt("kiln_data4", this.dataExcessFuelPaid);
        output.putInt("kiln_data3", this.dataOil);
        output.putInt("kiln_data2", this.dataBigTicksTarget);
        output.putInt("kiln_data1", this.dataBigTicksDone);
        output.putInt("kiln_data0", this.dataStatus);
        super.saveInternal(output);
    }

    // these 3 are for Jade.
    public ItemStack getOutput() { return this.getStoredItem(KilnMenu.SLOT_RESULT); }
    public ItemStack getByproduct() { return this.getStoredItem(KilnMenu.SLOT_BYPRODUCT); }
    public int getOilVolume() { return this.dataOil; }

    public void awardXP(Player player, boolean isByproductSlot)
    {
        double amount = isByproductSlot ? 0.3 : 1.8;
        amount *= Config.kiln_xp_multiplier();
        int count;
        if (isByproductSlot)
        {
            count = this.dataXp2;
            this.dataXp2 = 0;
        }
        else
        {
            count = this.dataXp1;
            this.dataXp1 = 0;
        }
        amount *= count;
        int amountAsInt = Mth.floor(amount);
        amount = Mth.frac(amount);
        if (amount > 0.0 && Math.random() < amount)
        {
            amountAsInt += 1;
        }
        player.giveExperiencePoints(amountAsInt);
    }

    private static class MostlySimpleContainer extends SimpleContainer
    {
        public MostlySimpleContainer(int containerCapacity, StandardContainerBlockEntity owner)
        {
            super(containerCapacity);
            this.owner = owner;
        }
        private final StandardContainerBlockEntity owner;

        @Override
        public void setChanged()
        {
            super.setChanged();
            this.owner.setChanged();
        }
    }
}

