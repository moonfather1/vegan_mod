package moonfather.vegan_mod;

import com.mojang.serialization.MapCodec;
import moonfather.vegan_mod.another_attempt_at_fluid.FluidRegistries;
import moonfather.vegan_mod.blocks.*;
import moonfather.vegan_mod.changes.SheddingHandler;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.function.Supplier;

@Mod(VeganMod.MODID)
public class VeganMod
{
    public static final String MODID = "vegan_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VeganMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        Blocks.init(modEventBus);
        Items.init(modEventBus);
        Other.init(modEventBus);
        BlockEntities.init(modEventBus);
        //FluidRegistration.init(modEventBus);
        FluidRegistries.init(modEventBus);

        modEventBus.addListener(DataMapManager::registerDataMapTypes);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "i_dont_want_to_kill_them_._serverconfig.toml");
        NeoForge.EVENT_BUS.addListener(SheddingHandler::onEntityTick);
        NeoForge.EVENT_BUS.addListener(SheddingHandler::onEntityRightClick);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        if (Config.litterEnabled())
        {
            NeoForge.EVENT_BUS.addListener(LitterManager::onEntityTick);
        }
    }

    public static class Other
    {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, VeganMod.MODID);
        private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, VeganMod.MODID);
        private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, VeganMod.MODID);

        public static final Supplier<RecipeSerializer<ArmorUncraftingRecipe>> ARMOR_RECIPE = RECIPES.register("armor_cutting", () -> ArmorUncraftingRecipe.SERIALIZER);

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
        public static void init(IEventBus modEventBus)
        {
            ITEMS.register(modEventBus);
            BLOCKS.register(modEventBus);
            modEventBus.addListener(Blocks::addCreative);
        }

        public static final DeferredBlock<Block> DRYING_RACK;
        public static final DeferredItem<Item> DRYING_RACK_ITEM;
        public static final DeferredBlock<Block> LITTER_OF_FEATHERS;
        public static final DeferredBlock<Block> KILN;
        public static final DeferredItem<Item> KILN_ITEM;
        static
        {
            final String shortName1 = "drying_rack";
            DRYING_RACK = BLOCKS.register(shortName1, () -> new DryingRackBlock(shortName1));
            DRYING_RACK_ITEM = ITEMS.register(shortName1, () -> new BlockItem(DRYING_RACK.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, shortName1)))));
            final String shortName2 = "litter_of_feathers";
            LITTER_OF_FEATHERS = BLOCKS.register(shortName2, () -> new LitterBlock(net.minecraft.world.item.Items.FEATHER, true, shortName2));
            final String shortName3 = "kiln";
            KILN = BLOCKS.register(shortName3, () -> new KilnBlock(shortName3));
            KILN_ITEM = ITEMS.register("kiln", () -> new KilnPlacerItem(KILN.get(), shortName3));
        }



        private static void addCreative(BuildCreativeModeTabContentsEvent event)
        {
            if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
            {
                event.accept(DRYING_RACK_ITEM);
                event.accept(KILN_ITEM);
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
        public static final DeferredItem<Item> PLANT_OIL = registerBottleItem("plant_oil");
        public static final DeferredItem<Item> PLANT_INK = registerBottleItem("plant_ink");
        public static final DeferredItem<Item> GLOWING_INK = registerBottleItem("glowing_ink");
        public static final DeferredItem<Item> HARDENED_FABRIC = registerItem("hardened_fabric");
        public static final DeferredItem<Item> RAW_FABRIC = registerItem("raw_fabric");
        public static final DeferredItem<Item> THICK_OIL = registerBottleItem("thick_oil");

        private static void addCreative(BuildCreativeModeTabContentsEvent event)
        {
            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS)
            {
                event.accept(HARDENED_FABRIC);
                event.accept(RAW_FABRIC);
                event.accept(PLANT_INK);
                event.accept(GLOWING_INK);
                event.accept(PLANT_OIL);
                event.accept(THICK_OIL);
            }
        }

        private static DeferredItem<Item> registerItem(String id)
        {
            ResourceKey<Item> bigId = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, id));
            Item.Properties prop = new Item.Properties().setId(bigId);
            return ITEMS.register(id, () -> new Item(prop));
        }
        private static DeferredItem<Item> registerBottleItem(String id)
        {
            ResourceKey<Item> bigId = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, id));
            Item.Properties prop = new Item.Properties().setId(bigId).craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE).stacksTo(16);
            return ITEMS.register(id, () -> new Item(prop));
        }
    }



    public static class BlockEntities
    {
        private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VeganMod.MODID);
        private static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, VeganMod.MODID);

        public static final Supplier<BlockEntityType<KilnBlockEntity>> KILN_BE = BLOCK_ENTITIES.register("kiln_be", () -> new BlockEntityType<>(KilnBlockEntity::new, Blocks.KILN.get()));
        public static final Supplier<MenuType<KilnMenu>> KILN_MENU_TYPE = CONTAINER_TYPES.register("crafting_single", () -> IMenuTypeExtension.create(KilnMenu::new));
        public static final Supplier<BlockEntityType<DryingRackBlockEntity>> DRYING_RACK_BE = BLOCK_ENTITIES.register("drying_rack_be", () -> new BlockEntityType<>(DryingRackBlockEntity::new, Blocks.DRYING_RACK.get()));


        public static void init(IEventBus modEventBus)
        {
            BLOCK_ENTITIES.register(modEventBus);
            CONTAINER_TYPES.register(modEventBus);
        }
    }
}
