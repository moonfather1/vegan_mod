package moonfather.vegan_mod;

import moonfather.vegan_mod.changes.RecipeManagerMain;
import moonfather.vegan_mod.items.ArmorCraftingRecipe;
import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import moonfather.vegan_mod.items.FullBottleItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
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
		Items.initialize();
		Other.initialize();
		// https://wiki.fabricmc.net/tutorial:items#creating_items_in_1212

		// server starting in add to reload
		// remove 4 cutting. 5? 5.
		// + add manual armor recipe with comp
		// + add uncrafting
		// logo
		// book
		// optional for leather
		// ink2 dead

		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(MOD_ID, "green_armor"), ArmorCraftingRecipe.getSerializerForRegistration());
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(MOD_ID, "armor_cutting"), ArmorUncraftingRecipe.getSerializerForRegistration());
	}

	///////////////////////////////////////

	public static class Items
	{
		public static final Item HARDENED_FABRIC = new Item(new Item.Properties());
		public static final Item PLANT_OIL = new FullBottleItem();
		public static final Item PLANT_INK = new FullBottleItem();

		//////////////////////////////////////////////////////

		public static void initialize()
		{
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "hardened_fabric"), HARDENED_FABRIC);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_ink"), PLANT_INK);
			Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "plant_oil"), PLANT_OIL);

			ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(Items::addToCreativeTabs);

			ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(RecipeManagerMain::loaded);
		}

		private static void addToCreativeTabs(FabricItemGroupEntries entries)
		{
			entries.accept(HARDENED_FABRIC);
			entries.accept(PLANT_INK);
			entries.accept(PLANT_OIL);
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
