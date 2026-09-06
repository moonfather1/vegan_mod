package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class DryingRecipeManager
{
    public static DryingRecipe getRecipe(ItemStack itemOnRack)
    {
        for (DryingRecipe r : recipes)
        {
            if (r.getBaseItem().test(itemOnRack))
            {
                return r;
            }
        }
        return null;
    }

    public static void initialize(ServerPlayer serverPlayer, boolean joined)
    {
        if (serverPlayer.getServer() == null) { return; }
        recipes.clear();
        for (RecipeHolder<DryingRecipe> holder : serverPlayer.getServer().getRecipeManager().getAllRecipesFor(VeganMod.Other.DRYING_RECIPE_TYPE))
        {
            recipes.add(holder.value());
        }
    }

    public static void refresh()
    {
        recipes.clear();
    }
    private static final List<DryingRecipe> recipes = new ArrayList<>();
}
