package moonfather.vegan_mod.changes;

import net.minecraft.server.level.ServerPlayer;

public class RecipeManagerMain
{
    public static void beforeSync(ServerPlayer serverPlayer, boolean joined)
    {
        RecipeManagerForLeather.joined(serverPlayer, joined);
        RecipeManagerForInk.joined(serverPlayer, joined);
    }
}
