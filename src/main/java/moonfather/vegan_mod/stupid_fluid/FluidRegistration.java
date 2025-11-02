package moonfather.vegan_mod.stupid_fluid;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FluidRegistration
{
    public static void registerFluidType(String name, Supplier<FluidType> typeSupplier) { TYPES.register(name, typeSupplier); }
    public static void registerFluid(String name, Supplier<? extends Fluid> fluidSupplier) { FLUIDS.register(name, fluidSupplier); }
    public static void registerBlock(String name, Supplier<LiquidBlock> blockSupplier) { BLOCKS.register(name, blockSupplier); }
    public static void registerItem(String name, Supplier<BucketItem> itemSupplier) { ITEMS.register(name, itemSupplier); }

    /////////////////////////////
    public static void init(IEventBus bus)
    {
        TYPES.register(bus);
        FLUIDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    private static final DeferredRegister<FluidType> TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, VeganMod.MODID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, VeganMod.MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, VeganMod.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, VeganMod.MODID);

    /////////////////////////

    public static final FluidDetails OIL_FLUID = new FluidDetails(
            "plant_oil",
            FluidType.Properties.create().canSwim(true).canDrown(true).canPushEntity(true).supportsBoating(true),
            null,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER),
            new Item.Properties().stacksTo(1)
    );
}
