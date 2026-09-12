package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

public class RecipeManagerForCharcoal extends RecipeReplacerForTheWholeThing
{
    @Override
    protected Recipe<?> replacement()
    {
        return new CharcoalReplacementRecipe();
    }

    @Override
    protected boolean shouldReplace(Identifier identifier, Recipe<?> value)
    {
        return identifier.getPath().equals("charcoal") && value.getType().equals(RecipeType.SMELTING);
    }
}
