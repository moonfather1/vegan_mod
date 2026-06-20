package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.items.ArmorManagement;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RepairItemRecipe.class)
public class ArmorRepairMixin
{
	@Inject(at = @At("HEAD"), method = "canCombine", cancellable = true)
	private static void checkComponents(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir)
	{
		if (first.has(VeganMod.Other.VEGAN_MARKER) != second.has(VeganMod.Other.VEGAN_MARKER))
		{
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("RETURN"), method = "assemble", cancellable = false)
	private void addComponents(CraftingInput craftingInput, CallbackInfoReturnable<ItemStack> cir)
	{
		ItemStack result = cir.getReturnValue();
		if (result.is(Items.LEATHER_HELMET) || result.is(Items.LEATHER_CHESTPLATE) || result.is(Items.LEATHER_LEGGINGS) || result.is(Items.LEATHER_BOOTS))
		{
			if (craftingInput.getItem(0).has(VeganMod.Other.VEGAN_MARKER))
			{
				result.set(DataComponents.LORE, new ItemLore(List.of(ArmorManagement.getLore())));
				result.set(DataComponents.DYED_COLOR, new DyedItemColor(ArmorManagement.getArmorColor()));
				result.set(VeganMod.Other.VEGAN_MARKER, Unit.INSTANCE);
			}
		}
	}
}