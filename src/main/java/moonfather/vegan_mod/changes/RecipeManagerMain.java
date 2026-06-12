package moonfather.vegan_mod.changes;

import net.minecraft.world.item.crafting.RecipeManager;

public class RecipeManagerMain
{
    public static void beforeSync(RecipeManager recipeManager)
    {
        (new RecipeManagerForLeather()).replace(recipeManager);
        (new RecipeManagerForInk()).replace(recipeManager);
        (new RecipeManagerForRabbitHide()).replace(recipeManager);
        // third one works but not entirely
        //RecipeManagerForCuttingBoard.joined(serverPlayer, joined);
    }
}
