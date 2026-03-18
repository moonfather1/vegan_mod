package moonfather.vegan_mod.another_attempt_at_fluid;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidRegistries
{
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, VeganMod.MODID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, VeganMod.MODID);
    private static final DeferredRegister.Blocks FLUID_BLOCKS = DeferredRegister.createBlocks(VeganMod.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, VeganMod.MODID);

    public static void init(IEventBus bus)
    {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
        FLUID_BLOCKS.register(bus);
        ITEMS.register(bus);
        bus.addListener(FluidRegistries::addCreative);
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
        {
            event.insertAfter(Items.MILK_BUCKET.getDefaultInstance(), OIL_BUCKET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------//

    public static final DeferredHolder<Fluid, OilFluid.Flowing> OIL_FLOWING = FLUIDS.register("plant_oil_flowing", () -> OilFluid.FLOWING);
    public static final DeferredHolder<Fluid, OilFluid.Source> OIL_SOURCE = FLUIDS.register("plant_oil_source", () -> OilFluid.SOURCE);

    public static final DeferredHolder<FluidType, OilFluidType> OIL_TYPE = FLUID_TYPES.register("plant_oil", OilFluidType::new );

    public static final DeferredHolder<Block, LiquidBlock> OIL_BLOCK = FLUID_BLOCKS.register(
            "plant_oil_fluid",
            () -> new LiquidBlock(
                    OIL_SOURCE.get(),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.SAND)
                            .replaceable()
                            .noCollission()
                            .strength(100.0f)
                            .pushReaction(PushReaction.DESTROY)
                            .noLootTable()
                            .liquid()
                            .sound(SoundType.FROGSPAWN)
            )
            {
                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 90;
                }
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return true;
                }
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
                {
                    return 20;
                }
            }
    );

    public static final DeferredHolder<Item, BucketItem> OIL_BUCKET = ITEMS.register(
            "plant_oil_bucket",
            () -> new BucketItem(
                    OIL_SOURCE.get(), new Item.Properties()
                    .stacksTo(1)
                    .craftRemainder(Items.BUCKET)
            )
    );
}
