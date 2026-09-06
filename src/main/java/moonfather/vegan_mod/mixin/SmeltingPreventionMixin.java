package moonfather.vegan_mod.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractCookingRecipe.class)
public abstract class SmeltingPreventionMixin
{
    @ModifyReturnValue(method = "matches", at = @At("RETURN"))
    private boolean checkForLeather(boolean originalResult)
    {
        if (originalResult == false) { return false; }
        if (result.is(Items.LEATHER) && ingredient.test(unwantedInput)) { return false; }
        return true;
    }
    @Unique
    private final ItemStack unwantedInput = Items.ROTTEN_FLESH.getDefaultInstance();

    @Final
    @Shadow
    protected Ingredient ingredient;

    @Final
    @Shadow
    protected ItemStack result;
}