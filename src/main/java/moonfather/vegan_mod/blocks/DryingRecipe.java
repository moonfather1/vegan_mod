package moonfather.vegan_mod.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class DryingRecipe implements Recipe<SingleRecipeInput>
{
    private final ItemStackTemplate result;
    private final Ingredient baseItem;
    private final int timeInMinutes;

    public DryingRecipe(Ingredient baseItem, ItemStackTemplate result, int timeInMinutes)
    {
        this.baseItem = baseItem;
        this.result = result;
        this.timeInMinutes = timeInMinutes;
    }
    //////////////////

    public ItemStackTemplate getResult() { return this.result; }
    public ItemStack getResultForViewers() { return this.result.create(); }
    public Ingredient getBaseItem() { return this.baseItem; }
    public int getTimeInMinutes() { return Math.max(this.timeInMinutes, 1); }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level)
    {
        return this.baseItem.test(recipeInput.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput recipeInput)
    {
        return this.result.create();
    }

    @Override
    public boolean isSpecial() { return true; }

    @Override
    public boolean showNotification() { return false; }

    @Override
    public String group() { return ""; }

    //---------------------------------------------//


    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType()
    {
        return VeganMod.Other.DRYING_RECIPE_TYPE;
    }

    @Override
    public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }

    @Override
    public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.BLAST_FURNACE_MISC; }

    public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("base_item").forGetter(DryingRecipe::getBaseItem),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(DryingRecipe::getResult),
                    Codec.INT.fieldOf("time_in_minutes").forGetter(DryingRecipe::getTimeInMinutes)
            ).apply(instance, DryingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            DryingRecipe::getBaseItem,
            ItemStackTemplate.STREAM_CODEC,
            DryingRecipe::getResult,
            ByteBufCodecs.INT,
            DryingRecipe::getTimeInMinutes,
            DryingRecipe::new
    );

    public static final RecipeSerializer<DryingRecipe> SERIALIZER = new RecipeSerializer<DryingRecipe>(CODEC, STREAM_CODEC);

    @Override
    public RecipeSerializer<DryingRecipe> getSerializer()
    {
        return SERIALIZER;
    }
}
