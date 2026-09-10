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
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ReiCategoryCharcoal implements DisplayCategory<ReiCategoryCharcoal.KilnRecipeDisplay>
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "rei_plugin2");


    @Override
    public CategoryIdentifier<KilnRecipeDisplay> getCategoryIdentifier() { return CategoryIdentifier.of(ID); }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.vegan_mod.emi_category2"); }
    @Override
    public Renderer getIcon() { return ICON; }
    public static final EntryStack<ItemStack> ICON = EntryStack.of(new ItemEntryDefinition(), VeganMod.Blocks.KILN_ITEM.getDefaultInstance());

    /////////////////////////////////

    // Here is an example of the stone cutting category
    @Override
    public List<Widget> setupDisplay(KilnRecipeDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = new ArrayList();

        // The base background of the display
        // Please try to not remove this to preserve an uniform style to REI
        widgets.add(Widgets.createRecipeBase(bounds));

        // The gray arrow
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 13, startPoint.y + 4)));

        // We create a result slot background AND
        // disable the actual background of the slots, so the result slot can look bigger
//        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
//        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
//                .entries(display.getOutputEntries().get(0).castAsList()) // Get the first output ingredient
//                .disableBackground() // Disable the background because we have our bigger background
//                .markOutput()); // Mark this as the output for REI to identify
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 48, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0)) // Get the first output ingredient
                .markOutput()); // Mark this as the output for REI to identify
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 68, startPoint.y + 5))
                .entries(display.getOutputEntries().get(1)) // Get the first output ingredient
                .markOutput()); // Mark this as the output for REI to identify
        if (display.getOutputEntries().size() > 2)  {
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y + 5))
                .entries(display.getOutputEntries().get(2)) // Get the first output ingredient
                .markOutput()); } // Mark this as the output for REI to identify


        // We add the input slot
        widgets.add(Widgets.createSlot(new Point(startPoint.x - 10, startPoint.y + 5))
                .entries(display.getInputEntries().get(0)) // Get the first input ingredient
                .markInput()); // Mark this as the input for REI to identify

        // We return the list of widgets for REI to display
        return widgets;
    }


    @Override
    public int getDisplayHeight()
    {
        return 38;
    }

    public static class KilnRecipeDisplay implements Display
    {
        private List<EntryIngredient> list1 = null, list2 = null;
        @Override
        public List<EntryIngredient> getInputEntries()
        {
            if (list1 == null) { list1 = List.of( EntryIngredients.ofItemTag(ItemTags.LOGS_THAT_BURN) ); }
            return list1;
        }

        @Override
        public List<EntryIngredient> getOutputEntries()
        {
            ItemStack bottle = VeganMod.Items.THICK_OIL.getDefaultInstance();
            bottle.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.thick_oil2"));
            if (Config.kiln_gives_tar_paint())
            {
                ItemStack tar = new ItemStack(Items.BLACK_DYE);
                tar.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.black_paint"));
                return List.of(EntryIngredients.ofItemStacks(List.of(Items.CHARCOAL.getDefaultInstance())), EntryIngredients.ofItemStacks(List.of((bottle))), EntryIngredients.ofItemStacks(List.of((tar))));
            }
            return List.of(EntryIngredients.ofItemStacks(List.of(Items.CHARCOAL.getDefaultInstance())), EntryIngredients.ofItemStacks(List.of((bottle))));
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CategoryIdentifier.of(ID);
        }
    }
}
