package moonfather.vegan_mod.changes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;

public class RecipeManagerMain
{
    public static void beforeSync(RecipeManager recipeManager, RegistryAccess registryAccess)
    {
        RecipeManagerForLeather.joined(recipeManager, registryAccess);
        RecipeManagerForInk.joined(recipeManager, registryAccess);
        RecipeManagerForRabbitHide.joined(recipeManager, registryAccess);
        // third one works but not entirely
        //RecipeManagerForCuttingBoard.joined(serverPlayer, joined);
    }
}
