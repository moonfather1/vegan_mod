package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.items.ArmorManagement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShapedRecipe.class)
public class ArmorCraftingMixin
{
	@Inject(at = @At("RETURN"), method = "assemble", cancellable = true)
	private void addComponents(CraftingInput craftingInput, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> cir)
	{
		ItemStack result = cir.getReturnValue();
		if (result.is(Items.LEATHER_HELMET) || result.is(Items.LEATHER_CHESTPLATE) || result.is(Items.LEATHER_LEGGINGS) || result.is(Items.LEATHER_BOOTS))
		{
			if (craftingInput.getItem(0).is(VeganMod.Items.HARDENED_FABRIC))
			{
				result.set(DataComponents.LORE, new ItemLore(List.of(ArmorManagement.getLore())));
				result.set(DataComponents.DYED_COLOR, new DyedItemColor(ArmorManagement.getArmorColor(), false));
				result.set(VeganMod.Other.VEGAN_MARKER, Unit.INSTANCE);
			}
		}
	}
}