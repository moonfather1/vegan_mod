
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.OptionsCommon;
import moonfather.vegan_mod.VeganMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

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
                    continue;
                }
            }
            newList.add(recipe);
        }
        serverPlayer.getServer().getRecipeManager().replaceRecipes(newList);
    }
}
