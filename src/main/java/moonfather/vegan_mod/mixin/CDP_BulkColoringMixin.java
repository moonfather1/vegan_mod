package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import plus.dragons.createdragonsplus.integration.jei.category.FanColoringCategory;

import java.util.List;
import java.util.Optional;

@Pseudo
@Mixin(FanColoringCategory.class)
public abstract class CDP_BulkColoringMixin
{
    @Unique
    private static final ResourceLocation ourRecipe = ResourceLocation.fromNamespaceAndPath(VeganMod.MODID, "ink1_as_coloring");

    @Inject(at = @At("HEAD"), method = "convert2x1(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/DyeColor;Ljava/util/List;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;", cancellable = true)
    private static void addGoal(ResourceLocation id, DyeColor color, List<Ingredient> ingredients, ItemStack result, CallbackInfoReturnable<Optional<RecipeHolder<?>>> cir)
    {
        if (id.equals(ourRecipe))
        {
            cir.setReturnValue(Optional.empty());
        }
    }
}
