package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

public class RecipeManagerForLeather
{
    public static void joined(MinecraftServer server)
    {
        if (server == null) { return; }
        ItemStack leaVan = new ItemStack(Items.LEATHER), leaOur = new ItemStack(VeganMod.Items.HARDENED_FABRIC.get());
        Collection<RecipeHolder<?>> all = server.getRecipeManager().getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.value().getType().equals(RecipeType.CRAFTING))
            {
                if (! recipe.id().getPath().equals("leather_helmet") && ! recipe.id().getPath().equals("leather_chestplate") && ! recipe.id().getPath().equals("leather_leggings") && ! recipe.id().getPath().equals("leather_boots"))
                {
                    for (int i = 0; i < recipe.value().getIngredients().size(); i++)
                    {
                        if (recipe.value().getIngredients().get(i).test(leaVan) && ! recipe.value().getIngredients().get(i).test(leaOur))
                        {
                            Ingredient newIng = makeIngredient(recipe.value().getIngredients().get(i), leaOur);
                            recipe.value().getIngredients().set(i, newIng);
                        }
                    }
                }
            }
        }
    }


    /////////////////////////////

    public static Ingredient makeIngredient(Ingredient ingredient, ItemStack toAdd)
    {
        int hash = Arrays.hashCode(ingredient.getItems());
        if (map.containsKey(hash))
        {
            return map.get(hash);
        }
        List<ItemStack> list = new LinkedList<>();
        for (int j = 0; j < ingredient.getItems().length; j++) { list.add(ingredient.getItems()[j]); }
        list.add(toAdd);
        Ingredient result = Ingredient.of(list.toArray(new ItemStack[0]));
        map.put(hash, result);
        return result;
    }
    private static final Map<Integer, Ingredient> map = new HashMap<>();
}
