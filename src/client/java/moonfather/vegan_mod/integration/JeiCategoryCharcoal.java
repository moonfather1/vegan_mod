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
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class JeiCategoryCharcoal extends AbstractRecipeCategory<JeiCategoryCharcoal.CharcoalRecipeForJei>
{
    public JeiCategoryCharcoal(IGuiHelper guiHelper)
    {
        super(CHARCOAL_RECIPE_TYPE, VeganMod.Blocks.KILN_BLOCK.getName(), guiHelper.createDrawableItemLike(VeganMod.Blocks.KILN_ITEM), 125, 52);
    }
    public static final RecipeType<CharcoalRecipeForJei> CHARCOAL_RECIPE_TYPE = RecipeType.create(VeganMod.MOD_ID, "recipe_type2", CharcoalRecipeForJei.class);

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CharcoalRecipeForJei recipe, IFocusGroup focusGroup)
    {
        builder.addInputSlot(8, 8).setStandardSlotBackground().addIngredients(recipe.input).setSlotName("leftSlot");
        builder.addOutputSlot(62, 8).setOutputSlotBackground().addItemStack(recipe.output.getDefaultInstance());
        builder.addOutputSlot(86, 8).setStandardSlotBackground().addItemStacks(recipe.byproduct).setSlotName("rightSlot");
    }

    public static class CharcoalRecipeForJei
    {
        public Ingredient input;
        public Item output;
        public List<ItemStack> byproduct;
        public int timeInSeconds;
    }

    ///////////////////////////////////////////////////


    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CharcoalRecipeForJei recipe, IFocusGroup focuses)
    {
        int cookTimeSeconds = recipe.timeInSeconds;
        builder.addAnimatedRecipeArrow(60).setPosition(30, 7);
        builder.addAnimatedRecipeFlame(60).setPosition(8, 28);
        int xpAmount = (int)  Math.round(1.8 * Config.kiln_xp_multiplier());
        Component experienceString = Component.translatable("gui.jei.category.smelting.experience", xpAmount);
        ((ITextWidget)builder.addText(experienceString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)).setTextAlignment(HorizontalAlignment.RIGHT).setColor(-8355712);
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        ((ITextWidget)builder.addText(timeString, this.getWidth() - 20, 10).setPosition(0, 0, this.getWidth(), this.getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)).setTextAlignment(HorizontalAlignment.RIGHT).setTextAlignment(VerticalAlignment.BOTTOM).setColor(-8355712);
    }
}
