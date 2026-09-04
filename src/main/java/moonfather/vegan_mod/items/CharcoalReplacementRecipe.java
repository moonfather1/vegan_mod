package moonfather.vegan_mod.items;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class CharcoalReplacementRecipe extends AbstractCookingRecipe {
    public CharcoalReplacementRecipe(String group, CookingBookCategory cookingBookCategory, Ingredient ingredient, ItemStack itemStack, float f, int i) {
        super(VeganMod.Other.OUR_SMELTING_RECIPE_TYPE, group, cookingBookCategory, ingredient, itemStack, f, i);
    }

    public CharcoalReplacementRecipe() {
        this("group", CookingBookCategory.MISC, Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), Items.GREEN_CANDLE.getDefaultInstance(), 1f, 20);
    }

    public ItemStack getToastSymbol() { return new ItemStack(Blocks.ANDESITE); }

//    public RecipeSerializer<?> getSerializer() {  return RecipeSerializer.SMELTING_RECIPE; }
    public RecipeSerializer<?> getSerializer() {  return VeganMod.Other.OUR_SMELTING_RECIPE_SERIALIZER; }

    // RecipeSerializer<SmeltingRecipe> SMELTING_RECIPE = register("smelting", new SimpleCookingSerializer(SmeltingRecipe::new, 200));
    ////////////////////////////

    @Override
    public boolean matches(SingleRecipeInput singleRecipeInput, Level level)
    {
        return false;
    }
}
