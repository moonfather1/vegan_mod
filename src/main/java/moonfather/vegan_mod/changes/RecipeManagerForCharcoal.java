package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import moonfather.vegan_mod.mixin.RecipeHolderAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

public class RecipeManagerForCharcoal
{
    public static void joined(ServerPlayer serverPlayer, boolean joined)
    {
        if (! Config.kiln_enabled()) { return; }
        if (serverPlayer == null || serverPlayer.getServer() == null) { return; }
        Collection<RecipeHolder<?>> all = serverPlayer.getServer().getRecipeManager().getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.value().getType().equals(RecipeType.SMELTING))
            {
                if (recipe.id().getPath().equals("charcoal"))
                {
                    var accessor = (RecipeHolderAccessor<Recipe<?>>) (Object) recipe;
                    accessor.setValue(new CharcoalReplacementRecipe());
                    break;
                }
            }
        }
    }
}
