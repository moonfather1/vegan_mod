package moonfather.vegan_mod;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import moonfather.vegan_mod.blocks.*;
import moonfather.vegan_mod.changes.RecipeManagerMain;
import moonfather.vegan_mod.changes.ReloadCommandReimplementation;
import moonfather.vegan_mod.changes.SheddingHandler;
import moonfather.vegan_mod.items.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeganMod implements ModInitializer
{
	public static final String MOD_ID = "vegan_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		Items.initialize();
		Other.initialize();
        Blocks.initialize();
		// create crushing  --- 1.20.1
        // rrv on 26.1
        // create on 26.1
		///////////////////////
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(MOD_ID, "armor_cutting"), ArmorUncraftingRecipe.getSerializerForRegistration());
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes = ResourceConditionType.create(Identifier.fromNamespaceAndPath(MOD_ID, "optional"), OptionalRecipeCondition.CODEC);
		OptionalRecipeCondition.setType(conditionTypeForOptionalRecipes);
		ResourceConditions.register(conditionTypeForOptionalRecipes);
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes2 = ResourceConditionType.create(Identifier.fromNamespaceAndPath(MOD_ID, "tag_not_empty"), TagNotEmptyRecipeCondition.CODEC);
		TagNotEmptyRecipeCondition.setType(conditionTypeForOptionalRecipes2);
		ResourceConditions.register(conditionTypeForOptionalRecipes2);
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes3 = ResourceConditionType.create(Identifier.fromNamespaceAndPath(MOD_ID, "tag_empty"), TagEmptyRecipeCondition.CODEC);
		TagNotEmptyRecipeCondition.setType(conditionTypeForOptionalRecipes3);
		ResourceConditions.register(conditionTypeForOptionalRecipes3);
		//////////////////
		UseEntityCallback.EVENT.register(SheddingHandler::onRightClickEntity);
        /////////////////////
        ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, Config.SPEC, "i_dont_want_to_kill_them_._serverconfig.toml");
        //////////////////////
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(DryingRecipeManager::initialize);


//        CommonLifecycleEvents.TAGS_LOADED.register((regAccess, client) -> LOGGER.info("~~~ CommonLifecycleEvents.TAGS_LOADED / " + client) );
//        ServerLifecycleEvents.SERVER_STARTING.register((server) -> LOGGER.info("~~~ ServerLifecycleEvents.SERVER_STARTING"));
//        ServerLifecycleEvents.SERVER_STARTED.register((server) -> LOGGER.info("~~~ ServerLifecycleEvents.SERVER_STARTED"));
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, rm, suc) -> LOGGER.info("~~~ ServerLifecycleEvents.END_DATA_PACK_RELOAD"));
//        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((serverPl, jo) -> LOGGER.info("~~~ ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS / " + jo));
        ServerLifecycleEvents.SERVER_STARTED.register(VeganMod::reloadResourcesBecauseOfTags);
        //////////////////
        ItemStorage.SIDED.registerForBlockEntity(KilnBlockEntity::getCapability, Blocks.KILN_BLOCK_ENTITY);
    }

    private static void reloadResourcesBecauseOfTags(MinecraftServer minecraftServer)
    {
        if (firstTimeFlag)
        {
            firstTimeFlag = false;
            ReloadCommandReimplementation.slashReload(minecraftServer);
        }
    }
    private static boolean firstTimeFlag = true;

	///////////////////////////////////////

	public static class Items
	{
		public static final IdentifiableItem HARDENED_FABRIC = new IdentifiableItem("hardened_fabric");
		public static final IdentifiableItem RAW_FABRIC = new IdentifiableItem("raw_fabric");
		public static final IdentifiableItem PLANT_OIL = new FullBottleItem("plant_oil");
		public static final IdentifiableItem THICK_OIL = new FullBottleItem("thick_oil");
		public static final IdentifiableItem PLANT_INK = new FullBottleItem("plant_ink");
		public static final IdentifiableItem GLOWING_INK = new FullBottleItem("glowing_ink");

		//////////////////////////////////////////////////////

		public static void initialize()
		{
            registerItem(HARDENED_FABRIC);
            registerItem(RAW_FABRIC);
            registerItem(PLANT_OIL);
            registerItem(THICK_OIL);
            registerItem(PLANT_INK);
            registerItem(GLOWING_INK);

            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(Items::addToCreativeTabs);

			ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(RecipeManagerMain::beforeSync);
		}

        private static void addToCreativeTabs(FabricCreativeModeTabOutput output)
		{
            output.accept(HARDENED_FABRIC);
            output.accept(RAW_FABRIC);
            output.accept(PLANT_OIL);
            output.accept(THICK_OIL);
            output.accept(PLANT_INK);
            output.accept(GLOWING_INK);
		}

        private static void registerItem(IdentifiableItem item)
        {
            Registry.register(BuiltInRegistries.ITEM, item.getMainId(), item);
        }

		private Items() {}
	}

	/////////////////////////////

	public static class Other
	{
		public static final DataComponentType<Unit> VEGAN_MARKER = DataComponentType.<Unit>builder().persistent(Unit.CODEC).build();

        private static final String OUR_SMELTING_RECIPE_ID = "smelting2";
        public static final RecipeType<CharcoalReplacementRecipe> OUR_SMELTING_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, OUR_SMELTING_RECIPE_ID),
                new RecipeType<CharcoalReplacementRecipe>() {
                    public String toString() { return OUR_SMELTING_RECIPE_ID; }
                }
            );
        //public static final SimpleCookingSerializer<CharcoalReplacementRecipe> OUR_SMELTING_RECIPE_SERIALIZER = new SimpleCookingSerializer<>(CharcoalReplacementRecipe::new, 2000);;

        public static final RecipeSerializer<DryingRecipe> DRYING_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                Identifier.fromNamespaceAndPath(MOD_ID, "drying"),
                DryingRecipe.SERIALIZER
        );

        public static final RecipeType<DryingRecipe> DRYING_RECIPE_TYPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                Identifier.fromNamespaceAndPath(MOD_ID, "drying"),
                new RecipeType<DryingRecipe>() { }
        );



        public static void initialize()
		{
			Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "vegan_made"), VEGAN_MARKER);
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, OUR_SMELTING_RECIPE_ID, CharcoalReplacementRecipe.SERIALIZER);
		}
		private Other() {}
	}

    ////////////////////////////////////////////////////////

    public static class Blocks
    {
        public static final Block DRYING_RACK_BLOCK = new DryingRackBlock("drying_rack");
        public static final Item DRYING_RACK_BLOCK_ITEM = new IdentifiableBlockItem(DRYING_RACK_BLOCK, "drying_rack", p -> p.cookingFuel(ContextIntProviders.COOKING_TIME_HANGING_SIGNS));
        public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.<DryingRackBlockEntity>create(DryingRackBlockEntity::new, DRYING_RACK_BLOCK).build();

        public static final Block LITTER_OF_FEATHERS = new LitterBlock(net.minecraft.world.item.Items.FEATHER, true, "litter_of_feathers");

        public static final Block KILN_BLOCK = new KilnBlock("kiln");
        public static final Item KILN_ITEM = new KilnPlacerItem(KILN_BLOCK, "kiln");
        public static final BlockEntityType<KilnBlockEntity> KILN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.<KilnBlockEntity>create(KilnBlockEntity::new, KILN_BLOCK).build();

        public static final MenuType<KilnMenu> KILN_MENU_TYPE = new MenuType<>(KilnMenu::new, FeatureFlagSet.of());



        private static void addToCreativeTabs(FabricCreativeModeTabOutput output)
        {
            output.accept(DRYING_RACK_BLOCK_ITEM);
            output.accept(KILN_ITEM);
        }

        public static void initialize()
        {
            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(Blocks::addToCreativeTabs);

            Identifier id1 = Identifier.fromNamespaceAndPath(MOD_ID, "drying_rack");
            Identifier id2 = Identifier.fromNamespaceAndPath(MOD_ID, "drying_rack_be");
            Registry.register(BuiltInRegistries.BLOCK, id1, DRYING_RACK_BLOCK);
            Registry.register(BuiltInRegistries.ITEM, id1, DRYING_RACK_BLOCK_ITEM);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id2, DRYING_RACK_BLOCK_ENTITY);

            Identifier id3 = Identifier.fromNamespaceAndPath(MOD_ID, "litter_of_feathers");
            Registry.register(BuiltInRegistries.BLOCK, id3, LITTER_OF_FEATHERS);

            FlammableBlockRegistry.getDefaultInstance().add(DRYING_RACK_BLOCK, 60, 20);
            FlammableBlockRegistry.getDefaultInstance().add(LITTER_OF_FEATHERS, 20, 60);

            Identifier id4 = Identifier.fromNamespaceAndPath(MOD_ID, "kiln");
            Identifier id5 = Identifier.fromNamespaceAndPath(MOD_ID, "kiln_be");
            Identifier id6 = Identifier.fromNamespaceAndPath(MOD_ID, "kiln_menu");
            Registry.register(BuiltInRegistries.BLOCK, id4, KILN_BLOCK);
            Registry.register(BuiltInRegistries.ITEM, id4, KILN_ITEM);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id5, KILN_BLOCK_ENTITY);
            Registry.register(BuiltInRegistries.MENU, id6, KILN_MENU_TYPE);
        }

        private Blocks() {}
    }
}
