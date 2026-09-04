
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import moonfather.vegan_mod.mixin.RecipeHolderAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipeManagerForCuttingBoard
{
    public static void joined(ServerPlayer serverPlayer, boolean joined)
    {
        if (serverPlayer == null || serverPlayer.getServer() == null) { return; }
        if (! FabricLoader.getInstance().isModLoaded("farmersdelight")) { return; }
        Collection<RecipeHolder<?>> all = serverPlayer.getServer().getRecipeManager().getRecipes();
        List<RecipeHolder<?>> newList = new ArrayList<>(all.size());
//        for (RecipeHolder<?> recipe : all)
//        {
//            if (recipe.id().getNamespace().equals(("farmersdelight")))
//            {
//                if (recipe.id().getPath().equals("cutting/leather_boots")
//                    || recipe.id().getPath().equals("cutting/leather_chestplate")
//                    || recipe.id().getPath().equals("cutting/leather_helmet")
//                    || recipe.id().getPath().equals("cutting/leather_horse_armor")
//                    || recipe.id().getPath().equals("cutting/leather_leggings") )
//                {
//                    continue;
//                }
//            }
//            newList.add(recipe);
//        }
//        serverPlayer.getServer().getRecipeManager().replaceRecipes(newList);
        int remaining = 5;
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.id().getNamespace().equals(("farmersdelight")))
            {
                if (recipe.id().getPath().equals("cutting/leather_boots")
                    || recipe.id().getPath().equals("cutting/leather_chestplate")
                    || recipe.id().getPath().equals("cutting/leather_helmet")
                    || recipe.id().getPath().equals("cutting/leather_horse_armor")
                    || recipe.id().getPath().equals("cutting/leather_leggings") )
                {
                    var accessor = (RecipeHolderAccessor<Recipe<?>>) (Object) recipe;
                    accessor.setValue(new CharcoalReplacementRecipe());  // could make a new type but whatever
                    remaining -= 1;
                    if (remaining == 0)
                    {
                        break;
                    }
                }
            }
        }
    }
}
