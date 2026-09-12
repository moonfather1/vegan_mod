package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import moonfather.vegan_mod.mixin.RecipeHolderAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Collection;

public abstract class RecipeReplacerForTheWholeThing
{
    public void replace(RecipeManager recipeManager)
    {
        if (recipeManager == null) { return; }
        this.initialize();
        Collection<RecipeHolder<?>> all = recipeManager.getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (this.shouldReplace(recipe.id().identifier(), recipe.value()))
            {
                var accessor = (RecipeHolderAccessor<Recipe<?>>) (Object) recipe;
                accessor.setValue(this.replacement());
                break;
            }
        }
    }

    protected void initialize() { }

    /// //////////////////////////////////

    protected abstract Recipe<?> replacement();
    protected abstract boolean shouldReplace(Identifier identifier, Recipe<?> value);
}
