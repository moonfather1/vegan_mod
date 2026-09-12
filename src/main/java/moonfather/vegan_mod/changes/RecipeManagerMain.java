package moonfather.vegan_mod.changes;

import net.minecraft.server.level.ServerPlayer;

public class RecipeManagerMain
{
    public static void beforeSync(ServerPlayer serverPlayer, boolean joined)
    {
        (new RecipeManagerForLeather()).replace(serverPlayer.level().recipeAccess());
        (new RecipeManagerForRabbitHide()).replace(serverPlayer.level().recipeAccess());
        (new RecipeManagerForInk()).replace(serverPlayer.level().recipeAccess());
        (new RecipeManagerForCharcoal()).replace(serverPlayer.level().recipeAccess());
        (new RecipeManagerForCuttingBoard()).replace(serverPlayer.level().recipeAccess());
    }
}
