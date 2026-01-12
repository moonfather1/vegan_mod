package moonfather.vegan_mod;

import com.mojang.serialization.MapCodec;
import moonfather.vegan_mod.another_attempt_at_fluid.FluidRegistries;
import moonfather.vegan_mod.blocks.DataMapManager;
import moonfather.vegan_mod.blocks.DryingRackBlock;
import moonfather.vegan_mod.blocks.DryingRackBlockEntity;
import moonfather.vegan_mod.changes.SheddingHandler;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import moonfather.vegan_mod.items.FullBottleItem;
import moonfather.vegan_mod.stupid_fluid.FluidRegistration;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.function.Supplier;

@Mod(VeganMod.MODID)
public class VeganMod
{
    public static final String MODID = "vegan_mod";
    public static final Logger LOGGER = LogUtils.getLogger();



    // todo: fluid density?, viscosity?, flammability, distance
    // todo: maybe - hardened oil
    // todo: maybe config for hiding fluid bucket (if no industry mods?)
    // todo: covered rack?(+msg),            check water next to tack?


    public VeganMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        Blocks.init(modEventBus);
        Items.init(modEventBus);
        Other.init(modEventBus);
        //FluidRegistration.init(modEventBus);
        FluidRegistries.init(modEventBus);

        modEventBus.addListener(DataMapManager::registerDataMapTypes);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "i_dont_want_to_kill_cows_._serverconfig.toml");
        NeoForge.EVENT_BUS.addListener(SheddingHandler::onEntityTick);
        NeoForge.EVENT_BUS.addListener(SheddingHandler::onEntityRightClick);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    public static class Other
    {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, VeganMod.MODID);
        private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, VeganMod.MODID);
        private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, VeganMod.MODID);

        public static final Supplier<RecipeSerializer<ArmorUncraftingRecipe>> ARMOR_RECIPE = RECIPES.register("armor_cutting", () -> new SimpleCraftingRecipeSerializer<ArmorUncraftingRecipe>(ArmorUncraftingRecipe::new));

        public static final Supplier<MapCodec<? extends ICondition>> OPTIONAL = CONDITIONS.register("optional", () -> OptionalRecipeCondition.CODEC);
        public static final Supplier<MapCodec<? extends ICondition>> THIRD_PARTY = CONDITIONS.register("third_party_condition", () -> ThirdPartyRecipeCondition.CODEC);
        public static final Supplier<DataComponentType<Unit>> VEGAN_MARKER = DATA_COMPONENT_TYPES.registerComponentType("vegan_made", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));



        public static void init(IEventBus modEventBus)
        {
            RECIPES.register(modEventBus);
            DATA_COMPONENT_TYPES.register(modEventBus);
            CONDITIONS.register(modEventBus);
        }
    }

    public static class Blocks
    {
        private Blocks() { }
        private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
        private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
        private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VeganMod.MODID);
        public static void init(IEventBus modEventBus)
        {
            ITEMS.register(modEventBus);
            BLOCKS.register(modEventBus);
            BLOCK_ENTITIES.register(modEventBus);
            modEventBus.addListener(Blocks::addCreative);
        }
        public static final DeferredBlock<Block> DRYING_RACK = BLOCKS.register("drying_rack", DryingRackBlock::new);
        public static final DeferredItem<Item> DRYING_RACK_ITEM = ITEMS.register("drying_rack", () -> new BlockItem(DRYING_RACK.get(), new Item.Properties()));
        public static final Supplier<BlockEntityType<DryingRackBlockEntity>> DRYING_RACK_BE = BLOCK_ENTITIES.register("drying_rack_be", () -> BlockEntityType.Builder.of(DryingRackBlockEntity::new, DRYING_RACK.get()).build(null));

        private static void addCreative(BuildCreativeModeTabContentsEvent event)
        {
            if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            {
                event.accept(DRYING_RACK_ITEM);
            }
        }
    }

    public static class Items
    {
        private Items() { }
        private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
        public static void init(IEventBus modEventBus)
        {
            ITEMS.register(modEventBus);
            modEventBus.addListener(Items::addCreative);
        }
        public static final DeferredItem<Item> PLANT_OIL = ITEMS.register("plant_oil", FullBottleItem::new);
        public static final DeferredItem<Item> PLANT_INK = ITEMS.register("plant_ink", FullBottleItem::new);
        public static final DeferredItem<Item> GLOWING_INK = ITEMS.register("glowing_ink", FullBottleItem::new);
        public static final DeferredItem<Item> HARDENED_FABRIC = ITEMS.register("hardened_fabric", () -> new Item(new Item.Properties()));
        public static final DeferredItem<Item> RAW_FABRIC = ITEMS.register("raw_fabric", () -> new Item(new Item.Properties()));
        private static void addCreative(BuildCreativeModeTabContentsEvent event)
        {
            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS)
            {
                event.accept(HARDENED_FABRIC);
                event.accept(RAW_FABRIC);
                event.accept(PLANT_INK);
                event.accept(PLANT_OIL);
                event.accept(GLOWING_INK);
            }
        }
    }

}
