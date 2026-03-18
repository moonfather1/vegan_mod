package moonfather.vegan_mod.integration;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class EmiRackRecipe extends BasicEmiRecipe
{
    public EmiRackRecipe(ResourceLocation id, Item input, Item output, int timeInMinutes)
    {
        super(EmiRegistration.RACK_RECIPE_CATEGORY, id, 125, 18);
        this.inputs.add(EmiStack.of(input));
        this.outputs.add(EmiStack.of(output));
        this.time = timeInMinutes;
    }
    private int time; // it needs time.

    @Override
    public void addWidgets(WidgetHolder widgetHolder)
    {

        // Add an arrow texture to indicate processing
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        // Adds an input slot on the left
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        // Adds an output slot on the right
        // Note that output slots need to call `recipeContext` to inform EMI about their recipe context
        // This includes being able to resolve recipe trees, favorite stacks with recipe context, and more
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}
