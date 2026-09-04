package moonfather.vegan_mod.mixin;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeHolder.class)
public interface RecipeHolderAccessor<R> {
    @Mutable
    @Accessor("value")
    void setValue(Recipe<?> newValue);
}

