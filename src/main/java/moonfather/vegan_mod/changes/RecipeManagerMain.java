package moonfather.vegan_mod.changes;

import net.minecraft.server.MinecraftServer;

public class RecipeManagerMain
{
    public static void beforeSync(MinecraftServer server)
    {
        RecipeManagerForLeather.joined(server);
        RecipeManagerForInk.joined(server);
        // third one works but not entirely
        //RecipeManagerForCuttingBoard.joined(serverPlayer, joined);
    }
}
