package moonfather.vegan_mod.integration;

import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.client.recipe.ClientRecipeManager;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.DryingRecipe;

public class RrvClientPlugin implements ReliableRecipeViewerClientPlugin
{
    @Override
    public void onIntegrationInitialize()
    {
        if (Config.leather_make_on_drying_rack())
        {
            ItemView.addClientRecipeProvider(recipeList -> { // provides a list of `ReliableClientRecipe`s to add to
                ClientRecipeManager.INSTANCE.getRecipesForType(VeganMod.Other.DRYING_RECIPE_TYPE).forEach(recipeHolder -> { // provides a list of `RecipeHolder<UpgradingRecipe>`s for you to convert
                    DryingRecipe recipe = recipeHolder.value();
                    recipeList.add(new RrvRecipeDrying(recipe.getBaseItem(), recipe.getResultForViewers(), recipe.getTimeInMinutes(), recipeHolder.id().identifier()));
                });
            });
        }

        if (Config.kiln_enabled())
        {
            ItemView.addClientRecipeProvider(recipeList -> recipeList.add(new RrvRecipeCharcoal()));
        }
    }
}
