package moonfather.vegan_mod.changes;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public class RecipeManagerMain
{
    public static void loaded(MinecraftServer server, CloseableResourceManager resourceManager, boolean success)
    {
        RecipeManagerForLeather.loaded(server, resourceManager, success);
    }
}
