package moonfather.vegan_mod;

import moonfather.vegan_mod.changes.RecipeManagerMain;
import moonfather.vegan_mod.changes.SheddingHandler;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import moonfather.vegan_mod.items.FullBottleItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeganMod implements ModInitializer
{
	public static final String MOD_ID = "vegan_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		// see about FD
		// ink2 is weird
		// options are dummies
		// shed timer
		Items.initialize();
		Other.initialize();
		// https://wiki.fabricmc.net/tutorial:items#creating_items_in_1212

		// remove 4 cutting.
		// logo
		// ink2 dead
		// options
		// simple mode
		// create crushing  --- 1.20.1
		// 1.21.8  models, recipes different,  resin
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
		//////////////////
		UseEntityCallback.EVENT.register(SheddingHandler::onRightClickEntity);

	}

	///////////////////////////////////////

	public static class Items
	{
		public static final Item HARDENED_FABRIC = new Item(new Item.Properties());
		public static final Item PLANT_OIL = new FullBottleItem();
		public static final Item PLANT_INK = new FullBottleItem();
		public static final Item GLOWING_INK = new FullBottleItem();

		//////////////////////////////////////////////////////

		public static void initialize()
		{
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hardened_fabric"), HARDENED_FABRIC);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_ink"), PLANT_INK);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_oil"), PLANT_OIL);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "glowing_ink"), GLOWING_INK);

			ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(Items::addToCreativeTabs);

			ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(RecipeManagerMain::beforeSync);
		}

		private static void addToCreativeTabs(FabricItemGroupEntries entries)
		{
			entries.accept(HARDENED_FABRIC);
			entries.accept(PLANT_INK);
			entries.accept(PLANT_OIL);
			entries.accept(GLOWING_INK);
		}

		private Items() {}
	}

	/////////////////////////////

	public static class Other
	{
		public static final DataComponentType<Unit> VEGAN_MARKER = DataComponentType.<Unit>builder().persistent(Unit.CODEC).build();

		public static void initialize()
		{
			Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "vegan_made"), VEGAN_MARKER);
		}
		private Other() {}
	}
}
