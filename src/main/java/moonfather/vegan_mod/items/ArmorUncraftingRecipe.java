package moonfather.vegan_mod.items;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmorUncraftingRecipe extends CustomRecipe
{
    public ArmorUncraftingRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }
    public ArmorUncraftingRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level)
    {
        if (craftingInput.width() != 1 || craftingInput.height() != 1)  { return false; }
        return getStartingLeatherAmount(craftingInput.getItem(0)) > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider)
    {
        if (craftingInput.width() != 1 || craftingInput.height() != 1)  { return ItemStack.EMPTY; }
        ItemStack input = craftingInput.getItem(0);
        return splitIntoLeather(input);
    }

    @NotNull
    public static ItemStack splitIntoLeather(ItemStack input)
    {
        int initialLeatherAmount = getStartingLeatherAmount(input);
        if (initialLeatherAmount == 0) { return ItemStack.EMPTY; } // should never happen
        double resultAmount = initialLeatherAmount;
        resultAmount *= (input.getMaxDamage() - input.getDamageValue()) / (double) input.getMaxDamage();
        resultAmount *= Config.leather_multiplier(); // half
        if (resultAmount < 1)  { return ItemStack.EMPTY; }
        int resultAmountAsInt = (int) Math.floor(resultAmount);
        ItemStack result = new ItemStack(input.has(VeganMod.Other.VEGAN_MARKER) ? VeganMod.Items.HARDENED_FABRIC : Items.LEATHER);
        result.setCount(resultAmountAsInt);
        //result.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("res is " + resultAmount))));
        return result;
    }

    private static int getStartingLeatherAmount(ItemStack input)
    {
        if (input.is(Items.LEATHER_HELMET)) return 5;
        if (input.is(Items.LEATHER_CHESTPLATE)) return 8;
        if (input.is(Items.LEATHER_LEGGINGS)) return 7;
        if (input.is(Items.LEATHER_BOOTS)) return 4;
        if (input.is(Items.LEATHER_HORSE_ARMOR)) return 4;
        return 0;
    }
    @Override
    public boolean canCraftInDimensions(int i, int j)
    {
        return i >= 1 && j >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return VeganMod.Other.ARMOR_RECIPE.get();
    }
}