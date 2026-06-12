package moonfather.vegan_mod.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.DataMapManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.BaseMappedRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class JeiRegistration implements IModPlugin
{
    @Override
    public Identifier getPluginUid()
    {
        return ID;
    }
    public static final Identifier ID = Identifier.fromNamespaceAndPath(VeganMod.MODID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new JeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        if (! Config.leather_make_on_drying_rack())
        {
            return;
        }
        if (BuiltInRegistries.ITEM instanceof BaseMappedRegistry bmr)
        {
            List<JeiCategory.DryingRecipeForJei> list = new ArrayList<>();
            Map<ResourceKey<Item>, DataMapManager.DryingRecipe> map = bmr.getDataMap(DataMapManager.DRYING_RECIPE);
            for (Map.Entry<ResourceKey<Item>, DataMapManager.DryingRecipe> entry : map.entrySet())
            {
                //System.out.printf("~~ %s -> %s in %d  %n ", entry.getKey().location(), entry.getValue().output().getRegisteredName(), entry.getValue().timeInMinutes());
                JeiCategory.DryingRecipeForJei recipe = new JeiCategory.DryingRecipeForJei();
                recipe.input = ((Holder.Reference<Item>) bmr.get(entry.getKey()).get()).value();  recipe.output = entry.getValue().output().value();   recipe.timeInMinutes = entry.getValue().timeInMinutes();
                list.add(recipe);
            }
            registration.addRecipes(JeiCategory.DRYING_RECIPE_TYPE, list);
        }
    }
}
