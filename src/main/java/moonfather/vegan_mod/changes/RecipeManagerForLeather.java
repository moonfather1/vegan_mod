package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

public class RecipeManagerForLeather extends RecipeManagerBase
{
    @Override
    protected ItemStack whatToReplace()
    {
        return new ItemStack(Items.LEATHER);
    }

    @Override
    protected ItemStack replacement()
    {
        return new ItemStack(VeganMod.Items.HARDENED_FABRIC.get());
    }

    @Override
    protected boolean shouldSkip(Identifier identifier, Recipe<?> value)
    {
        return identifier.getPath().equals("leather_helmet") || identifier.getPath().equals("leather_chestplate") || identifier.getPath().equals("leather_leggings") || identifier.getPath().equals("leather_boots");
    }
}
