package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;

public class RecipeManagerForRabbitHide
{
    public static void joined(ServerPlayer serverPlayer, boolean joined)
    {
        if (serverPlayer == null || serverPlayer.getServer() == null) { return; }
        ItemStack hide = new ItemStack(Items.RABBIT_HIDE), our = new ItemStack(VeganMod.Items.HARDENED_FABRIC);
        Collection<RecipeHolder<?>> all = serverPlayer.getServer().getRecipeManager().getRecipes();
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
    private static final ResourceLocation four2one = ResourceLocation.withDefaultNamespace("leather");  }
