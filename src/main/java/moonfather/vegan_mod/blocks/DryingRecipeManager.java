package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.HashMap;
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
        if (serverPlayer.level() == null) { return; }
        recipes.clear();
        for (RecipeHolder<DryingRecipe> holder : serverPlayer.level().recipeAccess().getAllOfType(VeganMod.Other.DRYING_RECIPE_TYPE))
        {
            recipes.put(holder.id().identifier(), holder.value());
        }
    }

    public static void refresh()
    {
        recipes.clear();
    }
    private static final Map<Identifier, DryingRecipe> recipes = new HashMap<>();

    public static Map<Identifier, DryingRecipe> getAll()
    {
        //LoggerFactory.getLogger(VeganMod.MOD_ID).info("~~~ DRM / " + recipes.size());
        return recipes;
    } // no need for immu, this is for client only
}
