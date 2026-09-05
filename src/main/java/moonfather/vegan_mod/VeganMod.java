package moonfather.vegan_mod;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
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
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
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
		// ink2 dead or weird
		// create crushing  --- 1.20.1
        // anvil repair mat on both platforn.  minor issue.
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

        public static void initialize()
		{
			Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "vegan_made"), VEGAN_MARKER);
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, OUR_SMELTING_RECIPE_ID, OUR_SMELTING_RECIPE_SERIALIZER);
		}
		private Other() {}
	}
}
