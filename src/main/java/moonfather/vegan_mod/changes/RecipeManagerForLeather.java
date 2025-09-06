package moonfather.vegan_mod.changes;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;

public class RecipeManagerForLeather
{
    public static void loaded(MinecraftServer server, CloseableResourceManager resourceManager, boolean success)
    {
        if (! success)
        {
            return;
        }
        Collection<RecipeHolder<?>> all = server.getRecipeManager().getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.id().getPath().contains("leather"))
            {
                System.out.println("~~~ " + recipe.id());
            }
        }
    }
}
