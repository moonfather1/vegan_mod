package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.items.ArmorUncraftingRecipe;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;

@Pseudo
@Mixin(CuttingBoardRecipe.class)
public class CuttingBoardMixin
{
	@Inject(at = @At("HEAD"), method = "matches", cancellable = true, remap = false)
	private void matchesWithoutArmor(CuttingBoardRecipeInput input, Level level, CallbackInfoReturnable<Boolean> cir)
	{
		if (input.item().is(Items.LEATHER_HELMET) || input.item().is(Items.LEATHER_CHESTPLATE) || input.item().is(Items.LEATHER_LEGGINGS) || input.item().is(Items.LEATHER_BOOTS) || input.item().is(Items.LEATHER_HORSE_ARMOR))
		{
			cir.setReturnValue(false);
		}
	}

//	//@Inject(at = @At("HEAD"), method = "Lvectorwing/farmersdelight/common/crafting/CuttingBoardRecipe;assemble(Lvectorwing/farmersdelight/common/crafting/CuttingBoardRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", cancellable = true, remap = false)
//	@Inject(at = @At("HEAD"), method = "assemble*", cancellable = true, remap = false)
//	private void cutUp(CuttingBoardRecipeInput input, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> cir)
//	{
//		if (input.item().is(Items.LEATHER_HELMET) || input.item().is(Items.LEATHER_CHESTPLATE) || input.item().is(Items.LEATHER_LEGGINGS) || input.item().is(Items.LEATHER_BOOTS) || input.item().is(Items.LEATHER_HORSE_ARMOR))
//		{
//			if (input.tool().is(ConventionalItemTags.SHEARS_TOOLS))
//			{
//				cir.setReturnValue(ArmorUncraftingRecipe.splitIntoLeather(input.item()));
//			}
//		}
//	}
}