
package moonfather.vegan_mod.changes;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import java.util.Collection;

public class RecipeManagerForRabbitHide
{
    public static void joined(RecipeManager recipeManager, RegistryAccess registryAccess)
    {
        if (recipeManager == null) { return; }
        ItemStack hide = new ItemStack(Items.RABBIT_HIDE), our = new ItemStack(VeganMod.Items.HARDENED_FABRIC.get());
        Collection<RecipeHolder<?>> all = recipeManager.getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.id().equals(four2one)) { continue; }
            for (int i = 0; i < recipe.value().getIngredients().size(); i++)
            {
                if (recipe.value().getIngredients().get(i).test(hide) && ! recipe.value().getIngredients().get(i).test(our))
                {
                    Ingredient newIng = RecipeManagerForLeather.makeIngredient(recipe.value().getIngredients().get(i), our);
                    recipe.value().getIngredients().set(i, newIng);
                }
            }
        }
    }
    private static final ResourceLocation four2one = ResourceLocation.withDefaultNamespace("leather");
}
