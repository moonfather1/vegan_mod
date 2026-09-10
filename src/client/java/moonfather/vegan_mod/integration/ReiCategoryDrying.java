package moonfather.vegan_mod.integration;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.entry.ItemEntryDefinition;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.DryingRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ReiCategoryDrying implements DisplayCategory<ReiCategoryDrying.DryingRecipeDisplay>
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "rei_plugin1");


    @Override
    public CategoryIdentifier<ReiCategoryDrying.DryingRecipeDisplay> getCategoryIdentifier() { return CategoryIdentifier.of(ID); }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.vegan_mod.emi_category1"); }
    @Override
    public Renderer getIcon() { return ICON; }
    public static final EntryStack<ItemStack> ICON = EntryStack.of(new ItemEntryDefinition(), VeganMod.Blocks.DRYING_RACK_BLOCK_ITEM.getDefaultInstance());

    /////////////////////////////////

    // Here is an example of the stone cutting category
    @Override
    public List<Widget> setupDisplay(ReiCategoryDrying.DryingRecipeDisplay display, Rectangle bounds)
    {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 20);
        List<Widget> widgets = new ArrayList();

        // The base background of the display
        // Please try to not remove this to preserve an uniform style to REI
        widgets.add(Widgets.createRecipeBase(bounds));

        // The gray arrow
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));

        // We create a result slot background AND
        // disable the actual background of the slots, so the result slot can look bigger
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0)) // Get the first output ingredient
                .disableBackground() // Disable the background because we have our bigger background
                .markOutput()); // Mark this as the output for REI to identify

        // We add the input slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().get(0)) // Get the first input ingredient
                .markInput()); // Mark this as the input for REI to identify

        widgets.add(Widgets.createLabel(new Point(startPoint.x + 27+27, startPoint.y + 5 + 24), Component.translatable("emi.category.vegan_mod.time", display.getTime())));
        // We return the list of widgets for REI to display
        return widgets;
    }

    @Override
    public int getDisplayHeight()
    {
        return 48;
    }

    ////////////////////////////////////

    public static class DryingRecipeDisplay implements Display
    {
        private final Ingredient input;
        private final ItemStack output;
        private final int time;

        public DryingRecipeDisplay(DryingRecipe recipe)
        {
            this.input = recipe.getBaseItem();
            this.output = recipe.getResult();
            this.time = (int) Math.round(recipe.getTimeInMinutes() * 60 * Config.kiln_time_multiplier());
        }

        @Override
        public List<EntryIngredient> getInputEntries() {
            return List.of(EntryIngredients.ofIngredient(this.input));
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
            return List.of(EntryIngredients.ofItemStacks(List.of(this.output)));
        }

        public int getTime() { return time; }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier()
        {
            return CategoryIdentifier.of(ID);
        }
    }
}
