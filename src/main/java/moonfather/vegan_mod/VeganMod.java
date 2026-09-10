package moonfather.vegan_mod;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import moonfather.vegan_mod.blocks.*;
import moonfather.vegan_mod.changes.RecipeManagerMain;
import moonfather.vegan_mod.changes.SheddingHandler;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import moonfather.vegan_mod.items.FullBottleItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
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
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(MOD_ID, "armor_cutting"), ArmorUncraftingRecipe.getSerializerForRegistration());
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes = ResourceConditionType.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "optional"), OptionalRecipeCondition.CODEC);
		OptionalRecipeCondition.setType(conditionTypeForOptionalRecipes);
		ResourceConditions.register(conditionTypeForOptionalRecipes);
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes2 = ResourceConditionType.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "tag_not_empty"), TagNotEmptyRecipeCondition.CODEC);
		TagNotEmptyRecipeCondition.setType(conditionTypeForOptionalRecipes2);
		ResourceConditions.register(conditionTypeForOptionalRecipes2);
		//////////
		ResourceConditionType<?> conditionTypeForOptionalRecipes3 = ResourceConditionType.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "tag_empty"), TagEmptyRecipeCondition.CODEC);
		TagNotEmptyRecipeCondition.setType(conditionTypeForOptionalRecipes3);
		ResourceConditions.register(conditionTypeForOptionalRecipes3);
		//////////////////
		UseEntityCallback.EVENT.register(SheddingHandler::onRightClickEntity);
        /////////////////////
        NeoForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, Config.SPEC, "i_dont_want_to_kill_them_._serverconfig.toml");
        //////////////////////
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(DryingRecipeManager::initialize);
	}

	///////////////////////////////////////

	public static class Items
	{
		public static final Item HARDENED_FABRIC = new Item(new Item.Properties());
		public static final Item RAW_FABRIC = new Item(new Item.Properties());
		public static final Item PLANT_OIL = new FullBottleItem();
		public static final Item THICK_OIL = new FullBottleItem();
		public static final Item PLANT_INK = new FullBottleItem();
		public static final Item GLOWING_INK = new FullBottleItem();

		//////////////////////////////////////////////////////

		public static void initialize()
		{
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hardened_fabric"), HARDENED_FABRIC);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "raw_fabric"), RAW_FABRIC);
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_oil"), PLANT_OIL);
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "thick_oil"), THICK_OIL);
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_ink"), PLANT_INK);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "glowing_ink"), GLOWING_INK);

			ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(Items::addToCreativeTabs);

			ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(RecipeManagerMain::beforeSync);
		}

		private static void addToCreativeTabs(FabricItemGroupEntries entries)
		{
			entries.accept(HARDENED_FABRIC);
			entries.accept(RAW_FABRIC);
            entries.accept(PLANT_OIL);
            entries.accept(THICK_OIL);
            entries.accept(PLANT_INK);
			entries.accept(GLOWING_INK);
		}

		private Items() {}
	}

	/////////////////////////////

	public static class Other
	{
		public static final DataComponentType<Unit> VEGAN_MARKER = DataComponentType.<Unit>builder().persistent(Unit.CODEC).build();

        private static final String OUR_SMELTING_RECIPE_ID = "smelting2";
        public static final RecipeType<CharcoalReplacementRecipe> OUR_SMELTING_RECIPE_TYPE = Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, OUR_SMELTING_RECIPE_ID),
                new RecipeType<CharcoalReplacementRecipe>() {
                    public String toString() { return OUR_SMELTING_RECIPE_ID; }
                }
            );
        public static final SimpleCookingSerializer<CharcoalReplacementRecipe> OUR_SMELTING_RECIPE_SERIALIZER = new SimpleCookingSerializer<>(CharcoalReplacementRecipe::new, 2000);;

        public static final RecipeSerializer<DryingRecipe> DRYING_RECIPE_SERIALIZER = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "drying"),
                DryingRecipe.SERIALIZER
        );

        public static final RecipeType<DryingRecipe> DRYING_RECIPE_TYPE = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "drying"),
                new RecipeType<DryingRecipe>() { }
        );



        public static void initialize()
		{
			Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "vegan_made"), VEGAN_MARKER);
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, OUR_SMELTING_RECIPE_ID, OUR_SMELTING_RECIPE_SERIALIZER);
		}
		private Other() {}
	}

    ////////////////////////////////////////////////////////

    public static class Blocks
    {
        public static final Block DRYING_RACK_BLOCK = new DryingRackBlock();
        public static final Item DRYING_RACK_BLOCK_ITEM = new BlockItem(DRYING_RACK_BLOCK, new Item.Properties());;
        public static final BlockEntityType<DryingRackBlockEntity> DRYING_RACK_BLOCK_ENTITY = BlockEntityType.Builder.<DryingRackBlockEntity>of(DryingRackBlockEntity::new, DRYING_RACK_BLOCK).build();

        public static final Block LITTER_OF_FEATHERS = new LitterBlock(net.minecraft.world.item.Items.FEATHER, true);

        public static final Block KILN_BLOCK = new KilnBlock();
        public static final Item KILN_ITEM = new KilnPlacerItem(KILN_BLOCK, new Item.Properties());;
        public static final BlockEntityType<KilnBlockEntity> KILN_BLOCK_ENTITY = BlockEntityType.Builder.<KilnBlockEntity>of(KilnBlockEntity::new, KILN_BLOCK).build();

        public static final MenuType<KilnMenu> KILN_MENU_TYPE = new MenuType<>(KilnMenu::new, FeatureFlagSet.of());



        private static void addToCreativeTabs(FabricItemGroupEntries entries)
        {
            entries.accept(DRYING_RACK_BLOCK_ITEM);
            entries.accept(KILN_ITEM);
        }

        public static void initialize()
        {
            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(Blocks::addToCreativeTabs);

            ResourceLocation id1 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "drying_rack");
            ResourceLocation id2 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "drying_rack_be");
            Registry.register(BuiltInRegistries.BLOCK, id1, DRYING_RACK_BLOCK);
            Registry.register(BuiltInRegistries.ITEM, id1, DRYING_RACK_BLOCK_ITEM);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id2, DRYING_RACK_BLOCK_ENTITY);

            ResourceLocation id3 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "litter_of_feathers");
            Registry.register(BuiltInRegistries.BLOCK, id3, LITTER_OF_FEATHERS);

            ((FireBlock) net.minecraft.world.level.block.Blocks.FIRE).setFlammable(DRYING_RACK_BLOCK, 60, 20);
            ((FireBlock) net.minecraft.world.level.block.Blocks.FIRE).setFlammable(LITTER_OF_FEATHERS, 20, 60);

            ResourceLocation id4 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "kiln");
            ResourceLocation id5 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "kiln_be");
            ResourceLocation id6 = ResourceLocation.fromNamespaceAndPath(MOD_ID, "kiln_menu");
            Registry.register(BuiltInRegistries.BLOCK, id4, KILN_BLOCK);
            Registry.register(BuiltInRegistries.ITEM, id4, KILN_ITEM);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id5, KILN_BLOCK_ENTITY);
            Registry.register(BuiltInRegistries.MENU, id6, KILN_MENU_TYPE);

            FuelRegistry.INSTANCE.add(DRYING_RACK_BLOCK_ITEM, 300*4);
        }
        private Blocks() {}
    }
}
