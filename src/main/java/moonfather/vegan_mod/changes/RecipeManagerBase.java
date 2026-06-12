
package moonfather.vegan_mod.changes;
import moonfather.vegan_mod.mixin.*;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.*;

public abstract class RecipeManagerBase
{
    public void replace(RecipeManager recipeManager)
    {
        if (recipeManager == null) { return; }
        ItemStack target = this.whatToReplace();
        ItemStack our = this.replacement();
        Collection<RecipeHolder<?>> all = recipeManager.getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (this.shouldSkip(recipe.id().identifier(), recipe.value()))
            {
                continue;
            }
            else if (recipe.value() instanceof ShapedRecipe shaped)
            {
                boolean needToAlter = false;
                for (int i = 0; i < shaped.getIngredients().size(); i++)
                {
                    if (shaped.getIngredients().get(i).isPresent() && shaped.getIngredients().get(i).get().test(target) && ! shaped.getIngredients().get(i).get().test(our))
                    {
                        needToAlter = true;
                        break;
                    }
                }
                if (needToAlter)
                {
                    List<Optional<Ingredient>> replacement = new ArrayList<>(9);
                    for (int i = 0; i < shaped.getIngredients().size(); i++)
                    {
                        if (shaped.getIngredients().get(i).isEmpty())
                        {
                            replacement.add(Optional.empty());
                        }
                        else if (shaped.getIngredients().get(i).get().test(target) && ! shaped.getIngredients().get(i).get().test(our))
                        {
                            Ingredient newIng = makeIngredient(shaped.getIngredients().get(i).get(), our);
                            replacement.add(Optional.of(newIng));
                        }
                        else
                        {
                            replacement.add(shaped.getIngredients().get(i));
                        }
                    }
                    ShapedRecipePattern p = ((ShapedRecipeAccessor) shaped).vm$getPattern();
                    ((ShapedRecipePatternAccessor) (Object) p).vm$setIngredients(replacement);
                }
            }//--///////////////////////////
            else if (recipe.value() instanceof ShapelessRecipe shapeless)
            {
                List<Ingredient> ingredients = ((ShapelessRecipeAccessor) shapeless).vm$getIngredients();  // immutable.
                boolean needToAlter = false;
                for (int i = 0; i < ingredients.size(); i++)
                {
                    if (ingredients.get(i).test(target) && ! ingredients.get(i).test(our))
                    {
                        needToAlter = true;
                        break;
                    }
                }
                if (needToAlter)
                {
                    List<Ingredient> replacement = new ArrayList<>(9);
                    for (int i = 0; i < ingredients.size(); i++)
                    {
                        if (ingredients.get(i).test(target) && ! ingredients.get(i).test(our))
                        {
                            Ingredient newIng = makeIngredient(ingredients.get(i), our);
                            replacement.add(newIng);
                        }
                        else
                        {
                            replacement.add(ingredients.get(i));
                        }
                    }
                    ((ShapelessRecipeAccessor) shapeless).vm$setIngredients(replacement);
                    ((NormalCraftingRecipeAccessor) shapeless).vm$setPlacementInfo(null);  // recipe has ingredients. and then in matches check they ignore that and take placementInfo.ingredients
                }
            }//--////////////////////////////////
            else if (recipe.value() instanceof SingleItemRecipe single)
            {
                if (single.input().test(target) && ! single.input().test(our))
                {
                    ((SingleItemRecipeAccessor) single).vm$setInput(makeIngredient(single.input(), our));
                }
            }
        }
    }



    protected abstract ItemStack whatToReplace();
    protected abstract ItemStack replacement();

    protected boolean shouldSkip(Identifier identifier, Recipe<?> value)
    {
        return false;
    }

    /////////////////////////////

    private static Ingredient makeIngredient(Ingredient ingredient, ItemStack toAdd)
    {
        StringBuilder textKey = new StringBuilder();
        if (! ingredient.isCustom())
        {
            if (ingredient.getValues() instanceof HolderSet.Named<Item> named)
            {
                textKey.append(named.toString());
            }
            else
            {
                ingredient.getValues().forEach(itemHolder -> textKey.append(itemHolder.getKey().identifier()));
            }
        }
        else
        {
            textKey.append(ingredient.getCustomIngredient().hashCode());
        }
        int hash = textKey.toString().hashCode(); // huuh
        if (map.containsKey(hash))
        {
            return map.get(hash);
        }
        List<ItemLike> list = new LinkedList<>();
        ingredient.items().forEach(itemHolder -> { list.add(itemHolder.value()); } );
        list.add(toAdd.getItem());
        Ingredient result = Ingredient.of(list.stream());
        map.put(hash, result);
        return result;
    }
    private static final Map<Integer, Ingredient> map = new HashMap<>();
}
