package moonfather.vegan_mod;

import com.mojang.serialization.MapCodec;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import moonfather.vegan_mod.items.FullBottleItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
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
public class VeganMod {
    public static final String MODID = "vegan_mod";
    public static final Logger LOGGER = LogUtils.getLogger();




    public VeganMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        Items.init(modEventBus);
        Other.init(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, "i_dont_want_to_kill_cows_._serverconfig.toml");
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    public static class Other
    {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, VeganMod.MODID);
        private static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, VeganMod.MODID);
        private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, VeganMod.MODID);

        public static final Supplier<RecipeSerializer<ArmorUncraftingRecipe>> ARMOR_RECIPE = RECIPES.register("armor_cutting", () -> new SimpleCraftingRecipeSerializer<ArmorUncraftingRecipe>(ArmorUncraftingRecipe::new));

        public static final Supplier<MapCodec<? extends ICondition>> OPTIONAL = CONDITIONS.register("optional", () -> OptionalRecipeCondition.CODEC);
        public static final Supplier<DataComponentType<Unit>> VEGAN_MARKER = DATA_COMPONENT_TYPES.registerComponentType("vegan_made", builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));



        public static void init(IEventBus modEventBus)
        {
            RECIPES.register(modEventBus);
            DATA_COMPONENT_TYPES.register(modEventBus);
            CONDITIONS.register(modEventBus);
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
        private static void addCreative(BuildCreativeModeTabContentsEvent event)
        {
            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS)
            {
                event.accept(HARDENED_FABRIC);
                event.accept(PLANT_INK);
                event.accept(PLANT_OIL);
                event.accept(GLOWING_INK);
            }
        }
    }

}
