package moonfather.vegan_mod.items;

import com.mojang.serialization.MapCodec;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class CharcoalReplacementRecipe extends AbstractCookingRecipe {
    public CharcoalReplacementRecipe(CommonInfo commonInfo, CookingBookInfo cookingBookInfo, Ingredient ingredient, ItemStackTemplate itemStackTemplate, float f, int i)
    {
        super(commonInfo, cookingBookInfo, ingredient, itemStackTemplate, f, i);
    }

    public CharcoalReplacementRecipe() {
        this(new CommonInfo(false), new CookingBookInfo(CookingBookCategory.MISC, "none"), Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), new ItemStackTemplate(Items.DEAD_BUSH), 1f, 20);
    }

    public ItemStack getToastSymbol() { return new ItemStack(Blocks.ANDESITE); }

//    public RecipeSerializer<?> getSerializer() {  return RecipeSerializer.SMELTING_RECIPE; }

    // RecipeSerializer<SmeltingRecipe> SMELTING_RECIPE = register("smelting", new SimpleCookingSerializer(SmeltingRecipe::new, 200));
    public static final MapCodec<CharcoalReplacementRecipe> MAP_CODEC = cookingMapCodec(CharcoalReplacementRecipe::new, 12000);
    public static final StreamCodec<RegistryFriendlyByteBuf, CharcoalReplacementRecipe> STREAM_CODEC = cookingStreamCodec(CharcoalReplacementRecipe::new);
    public static final RecipeSerializer<CharcoalReplacementRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() { return SERIALIZER; }
    //public RecipeSerializer<?> getSerializer() {  return VeganMod.Other.OUR_SMELTING_RECIPE_SERIALIZER; }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() { return VeganMod.Other.OUR_SMELTING_RECIPE_TYPE; }

    @Override
    public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.FURNACE_MISC; }

    @Override
    protected Item furnaceIcon() { return Items.FURNACE; }

    @Override
    public boolean isSpecial() { return true; }

    ////////////////////////////

    @Override
    public boolean matches(SingleRecipeInput singleRecipeInput, Level level)
    {
        return false;
    }
}
