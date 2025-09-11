
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.OptionsCommon;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Collection;

public class RecipeManagerForInk
{
    public static void joined(ServerPlayer serverPlayer, boolean joined)
    {
        if (serverPlayer == null || serverPlayer.getServer() == null) { return; }
        ItemStack inkVan = new ItemStack(Items.INK_SAC), inkOur = new ItemStack(VeganMod.Items.PLANT_INK);
        Collection<RecipeHolder<?>> all = serverPlayer.getServer().getRecipeManager().getRecipes();
        for (RecipeHolder<?> recipe : all)
        {
            if (recipe.value().getType().equals(RecipeType.CRAFTING))
            {
                ItemStack output = recipe.value().getResultItem(serverPlayer.getServer().overworld().registryAccess());
                if (output.has(DataComponents.FOOD))
                {
                    continue; // FD adds some food with squid ink
                }
                if (output.is(Items.BLACK_DYE) && OptionsCommon.ink_accepts_blue_dye())
                {
                    continue; // can't give black dye if we accept multiple dyes
                }
                boolean needsInkFix = false;
                for (Ingredient ingredient : recipe.value().getIngredients())
                {
                    if (ingredient.test(inkVan) && ! ingredient.test(inkOur))
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
                    }
                }
            }
        }
    }
}
