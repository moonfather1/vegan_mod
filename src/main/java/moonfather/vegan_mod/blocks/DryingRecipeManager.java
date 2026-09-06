package moonfather.vegan_mod.blocks;

import com.google.common.collect.ImmutableList;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DryingRecipeManager
{
    public static DryingRecipe getRecipe(ItemStack itemOnRack)
    {
        for (DryingRecipe r : recipes.values())
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
            recipes.put(holder.id(), holder.value());
        }
    }

    public static void refresh()
    {
        recipes.clear();
    }
    private static final Map<ResourceLocation, DryingRecipe> recipes = new HashMap<>();

    public static Map<ResourceLocation, DryingRecipe> getAll() { return recipes; } // no need for immu, this is for client only
}
