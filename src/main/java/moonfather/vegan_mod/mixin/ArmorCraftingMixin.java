package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
				if (armor_color == 0)
				{
					try
					{
						armor_color = Integer.decode(Config.leather_armor_color());
					}
					catch (NumberFormatException ex)
					{
						armor_color = armor_color_default;
					}
				}
				result.set(DataComponents.LORE, new ItemLore(List.of(lore_line)));
				result.set(DataComponents.DYED_COLOR, new DyedItemColor(armor_color, false));
				result.set(VeganMod.Other.VEGAN_MARKER, Unit.INSTANCE);
			}
		}
	}
	@Unique
	private static final Component lore_line = Component.translatable("message.vegan_mod.armor_subtitle").withColor(0x339911);
	@Unique
	private static final int armor_color_default = 0x339911;
	@Unique
	private static int armor_color = 0;
}