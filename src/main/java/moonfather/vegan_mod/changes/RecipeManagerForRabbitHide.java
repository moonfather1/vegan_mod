
package moonfather.vegan_mod.changes;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

public class RecipeManagerForRabbitHide extends RecipeManagerBase
{
    @Override
    protected ItemStack whatToReplace()
    {
        return new ItemStack(Items.RABBIT_HIDE);
    }

    @Override
    protected ItemStack replacement()
    {
        return new ItemStack(VeganMod.Items.HARDENED_FABRIC.get());
    }

    @Override
    protected boolean shouldSkip(Identifier identifier, Recipe<?> value)
    {
        return identifier.equals(four2one);
    }
    private static final Identifier four2one = Identifier.withDefaultNamespace("leather");
}
