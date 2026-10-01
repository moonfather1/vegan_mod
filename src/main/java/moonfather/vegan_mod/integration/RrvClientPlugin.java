package moonfather.vegan_mod.integration;

import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.blocks.DataMapManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.BaseMappedRegistry;

import java.util.Map;

public class RrvClientPlugin implements ReliableRecipeViewerClientPlugin
{
    @Override
    public void onIntegrationInitialize()
    {
        if (Config.leather_make_on_drying_rack())
        {
            ItemView.addClientRecipeProvider(recipeList -> { // provides a list of `ReliableClientRecipe`s to add to
                if (BuiltInRegistries.ITEM instanceof BaseMappedRegistry bmr)
                {
                    int counter = 1;
                    Map<ResourceKey<Item>, DataMapManager.DryingRecipe> map = bmr.getDataMap(DataMapManager.DRYING_RECIPE);
                    for (Map.Entry<ResourceKey<Item>, DataMapManager.DryingRecipe> entry : map.entrySet())
                    {
                        //System.out.printf("~~ %s -> %s in %d  %n ", entry.getKey().location(), entry.getValue().output().getRegisteredName(), entry.getValue().timeInMinutes());
                        recipeList.add(new RrvRecipeDrying(Ingredient.of(((Holder.Reference<Item>) bmr.get(entry.getKey()).get()).value()), entry.getValue().output().value().getDefaultInstance(), entry.getValue().timeInMinutes(), Identifier.fromNamespaceAndPath("vegan_mod", "/dr"+counter)));
                        counter += 1;
                    }
                }
            });
        }

        if (Config.kiln_enabled())
        {
            ItemView.addClientRecipeProvider(recipeList -> recipeList.add(new RrvRecipeCharcoal()));
            if (ImmersiveEngineeringHelper.loaded())
            {
                ItemView.addClientRecipeProvider(recipeList -> recipeList.add(new RrvRecipeCoke()));
            }
        }
    }
}
