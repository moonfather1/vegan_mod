package moonfather.vegan_mod.mixin;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SingleItemRecipe.class)
public interface SingleItemRecipeAccessor
{
    @Accessor("input")
    @Mutable
    void vm$setInput(Ingredient newInput);
}
