package moonfather.vegan_mod.mixin;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ShapelessRecipe.class)
public interface ShapelessRecipeAccessor
{
    @Accessor("ingredients")
    List<Ingredient> vm$getIngredients();

    @Accessor("ingredients")
    @Mutable
    void vm$setIngredients(List<Ingredient> newList);
}