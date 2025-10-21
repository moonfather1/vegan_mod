
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;

public class RecipeManagerForInk
{
    public static void joined(MinecraftServer server)
    {
        if (server == null) { return; }
        ItemStack inkVan = new ItemStack(Items.INK_SAC), inkOur = new ItemStack(VeganMod.Items.PLANT_INK.get());
        ItemStack ink2Van = new ItemStack(Items.GLOW_BERRIES), ink2Our = new ItemStack(VeganMod.Items.GLOWING_INK.get());
        Collection<RecipeHolder<?>> all = server.getRecipeManager().getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.value().getType().equals(RecipeType.CRAFTING))
            {
                ItemStack output = recipe.value().getResultItem(server.overworld().registryAccess());
                if (output.has(DataComponents.FOOD))
                {
                    continue; // FD adds some food with squid ink
                }
                if (output.is(Items.BLACK_DYE) && Config.ink_accepts_blue_dye())
                {
                    continue; // can't give black dye if we accept multiple dyes
                }
                if (output.is(VeganMod.Items.GLOWING_INK))
                {
                    continue; // don't want a recursive recipe
                }
                boolean needsInkFix = false;
                for (Ingredient ingredient : recipe.value().getIngredients())
                {
                    if (ingredient.test(inkVan) && ! ingredient.test(inkOur)
                        || ingredient.test(ink2Van) && ! ingredient.test(ink2Our))
                    {
                        needsInkFix = true;
                        break;
                    }
                }
                if (needsInkFix)
                {
                    for (int i = 0; i < recipe.value().getIngredients().size(); i++)
                    {
                        if (recipe.value().getIngredients().get(i).test(inkVan) && ! recipe.value().getIngredients().get(i).test(inkOur))
                        {
                            Ingredient newIng = RecipeManagerForLeather.makeIngredient(recipe.value().getIngredients().get(i), inkOur);
                            recipe.value().getIngredients().set(i, newIng);
                        }
                        if (recipe.value().getIngredients().get(i).test(ink2Van) && ! recipe.value().getIngredients().get(i).test(ink2Our))
                        {
                            Ingredient newIng = RecipeManagerForLeather.makeIngredient(recipe.value().getIngredients().get(i), ink2Our);
                            recipe.value().getIngredients().set(i, newIng);
                        }
                    }
                }
            }
        }
    }
}
