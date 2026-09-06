package moonfather.vegan_mod.integration;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.ITextWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class JeiCategoryDrying extends AbstractRecipeCategory<JeiCategoryDrying.DryingRecipeForJei>
{
    public JeiCategoryDrying(IGuiHelper guiHelper)
    {
        super(DRYING_RECIPE_TYPE, VeganMod.Blocks.DRYING_RACK_BLOCK.getName(), guiHelper.createDrawableItemLike(VeganMod.Blocks.DRYING_RACK_BLOCK), 125, 52);
    }
    public static final RecipeType<DryingRecipeForJei> DRYING_RECIPE_TYPE = RecipeType.create(VeganMod.MOD_ID, "recipe_type", DryingRecipeForJei.class);

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipeForJei dryingRecipe, IFocusGroup focusGroup)
    {
        builder.addInputSlot(8, 8).addIngredients(dryingRecipe.input).setStandardSlotBackground().setSlotName("leftSlot");
        builder.addOutputSlot(62, 8).setOutputSlotBackground().addItemStacks(List.of(dryingRecipe.output));
    }

    public static class DryingRecipeForJei
    {
        public Ingredient input;
        public ItemStack output;
        public int timeInMinutes;
    }

    ///////////////////////////////////////////////////


    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, DryingRecipeForJei recipe, IFocusGroup focuses)
    {
        int cookTime = recipe.timeInMinutes;
        builder.addAnimatedRecipeArrow(cookTime * 60).setPosition(30, 6);
        int xpAmount = 5;
        Component experienceString = Component.translatable("gui.jei.category.smelting.experience", xpAmount);
        ((ITextWidget)builder.addText(experienceString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)).setTextAlignment(HorizontalAlignment.RIGHT).setColor(-8355712);
        int cookTimeSeconds = cookTime * 60;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        ((ITextWidget)builder.addText(timeString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)).setTextAlignment(HorizontalAlignment.RIGHT).setTextAlignment(VerticalAlignment.BOTTOM).setColor(-8355712);
    }
}
