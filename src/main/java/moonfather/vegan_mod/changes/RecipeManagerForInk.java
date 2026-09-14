
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

public class RecipeManagerForInk  extends RecipeReplacerForItems
{
    private int pass = 0; // black ink is 1, glow ink is 2

    @Override
    public void replace(RecipeManager recipeManager)
    {
        this.pass = 1;
        super.replace(recipeManager);
        this.pass = 2;
        super.replace(recipeManager);
    }

    @Override
    protected ItemStack whatToReplace()
    {
        if (pass == 1) return new ItemStack(Items.INK_SAC);
        if (pass == 2) return new ItemStack(Items.GLOW_INK_SAC);
        throw new UnsupportedOperationException();
    }

    @Override
    protected ItemStack replacement()
    {
        if (pass == 1) return new ItemStack(VeganMod.Items.PLANT_INK);
        if (pass == 2) return new ItemStack(VeganMod.Items.GLOWING_INK);
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean shouldSkip(Identifier identifier, Recipe<?> recipe)
    {
        ItemStack output = recipe.display() != null && recipe.display().size() > 0 ? recipe.display().get(0).result().resolveForFirstStack(CM_EMPTY) : ItemStack.EMPTY;
        if (output.has(DataComponents.FOOD))
        {
            return true; // FD adds some food with squid ink
        }
        if (output.is(Items.DYE.black()) && Config.ink_accepts_blue_dye())
        {
            return true; // can't give black dye if we accept multiple dyes
        }
        return  false;
    }
    private static final ContextKeySet CKS_EMPTY = (new ContextKeySet.Builder()).build();
    private static final ContextMap CM_EMPTY = (new ContextMap.Builder()).create(CKS_EMPTY);
}
