package moonfather.vegan_mod.integration;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.blocks.DryingRecipeManager;

public class ReiRegistration implements REIClientPlugin
{
    @Override
    public void registerCategories(CategoryRegistry registry)
    {
        ReiCategoryDrying c1 = new ReiCategoryDrying();
        registry.add(c1);
        registry.addWorkstations(c1.getCategoryIdentifier(), ReiCategoryDrying.ICON);

        ReiCategoryCharcoal c2 = new ReiCategoryCharcoal();
        registry.add(c2);
        registry.addWorkstations(c2.getCategoryIdentifier(), ReiCategoryCharcoal.ICON);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry)
    {
        // drying rack
        if (Config.leather_make_on_drying_rack())
        {
            for (var entry  : DryingRecipeManager.getAll().entrySet())
            {
                registry.add(new ReiCategoryDrying.DryingRecipeDisplay(entry.getValue()));
            }
        }
        // kiln
        if (Config.kiln_enabled())
        {
            registry.add(new ReiCategoryCharcoal.KilnRecipeDisplay());
        }
    }
}
