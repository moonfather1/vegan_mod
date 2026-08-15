package moonfather.vegan_mod.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.DataMapManager;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
        registration.addRecipeCategories(new JeiCategoryDrying(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JeiCategoryCharcoal(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        if (Config.leather_make_on_drying_rack())
        {
            if (BuiltInRegistries.ITEM instanceof BaseMappedRegistry bmr)
            {
                List<JeiCategoryDrying.DryingRecipeForJei> list = new ArrayList<>();
                Map<ResourceKey<Item>, DataMapManager.DryingRecipe> map = bmr.getDataMap(DataMapManager.DRYING_RECIPE);
                for (Map.Entry<ResourceKey<Item>, DataMapManager.DryingRecipe> entry : map.entrySet())
                {
                    //System.out.printf("~~ %s -> %s in %d  %n ", entry.getKey().location(), entry.getValue().output().getRegisteredName(), entry.getValue().timeInMinutes());
                    JeiCategoryDrying.DryingRecipeForJei recipe = new JeiCategoryDrying.DryingRecipeForJei();
                    recipe.input = ((Holder.Reference<Item>) bmr.get(entry.getKey()).get()).value();
                    recipe.output = entry.getValue().output().value();
                    recipe.timeInMinutes = entry.getValue().timeInMinutes();
                    list.add(recipe);
                }
                registration.addRecipes(JeiCategoryDrying.DRYING_RECIPE_TYPE, list);
            }
        }
        if (Config.kiln_enabled())
        {
            ItemStack bottle = VeganMod.Items.THICK_OIL.toStack();
            bottle.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.thick_oil2"));
            ItemStack tar = new ItemStack(Items.BLACK_DYE);
            tar.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.black_paint"));
            ItemStack bucket = ImmersiveEngineeringHelper.getBucketItem().getDefaultInstance();

            List<JeiCategoryCharcoal.CharcoalRecipeForJei> list = new ArrayList<>();
            JeiCategoryCharcoal.CharcoalRecipeForJei recipe = new JeiCategoryCharcoal.CharcoalRecipeForJei();
            recipe.input = makeIngredient(ItemTags.LOGS_THAT_BURN);  recipe.output = Items.CHARCOAL;   recipe.timeInSeconds = (int) Math.round(KilnBlockEntity.BASE_TIME_IN_SECONDS * Config.kiln_time_multiplier());
            recipe.byproduct = new ArrayList<>(3); recipe.byproduct.add(bottle); if (ImmersiveEngineeringHelper.loaded()) { recipe.byproduct.add(bucket); }  if (Config.kiln_gives_tar_paint()) { recipe.byproduct.add(tar); }
            list.add(recipe);
            if (ImmersiveEngineeringHelper.loaded())
            {
                recipe = new JeiCategoryCharcoal.CharcoalRecipeForJei();
                recipe.input = Ingredient.of(Items.COAL);  recipe.output = ImmersiveEngineeringHelper.getCoalItem();
                recipe.timeInSeconds = (int) Math.round(KilnBlockEntity.BASE_TIME_IN_SECONDS * Config.kiln_time_multiplier() * 1.5);
                recipe.byproduct = new ArrayList<>(3); recipe.byproduct.add(bottle);  recipe.byproduct.add(bucket);
                list.add(recipe);
            }
            registration.addRecipes(JeiCategoryCharcoal.CHARCOAL_RECIPE_TYPE, list);
        }
    }

    ///  because we can no longer write Ingredient.of(tagKey) ...   ffs...
    private static Ingredient makeIngredient(TagKey<Item> tag, HolderLookup.Provider lookupProvider)
    {
        return Ingredient.of(lookupProvider.lookupOrThrow(Registries.ITEM).getOrThrow(tag));
    }
    private static Ingredient makeIngredient(TagKey<Item> tag)
    {
        return Ingredient.of(Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(tag));
    }
}
