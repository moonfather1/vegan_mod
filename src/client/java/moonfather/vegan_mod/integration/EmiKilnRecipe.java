package moonfather.vegan_mod.integration;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EmiKilnRecipe extends BasicEmiRecipe
{
    public EmiKilnRecipe(ResourceLocation id, Item input, Item output, List<ItemStack> byproducts, int timeInMinutes)
    {
        super(EmiRegistration.KILN_RECIPE_CATEGORY, id, 125, 18);
        this.inputs.add(EmiStack.of(input));
        this.outputs.add(EmiStack.of(output));
        this.time = timeInMinutes;
        List<EmiStack> list = new ArrayList<>();
        byproducts.forEach(i -> list.add(EmiStack.of(i)));
        this.byproducts = EmiIngredient.of(list);
        this.outputs.addAll(list);
    }
    public EmiKilnRecipe(ResourceLocation id, TagKey<Item> inputs, Item output, List<ItemStack> byproducts, int timeInMinutes)
    {
        super(EmiRegistration.KILN_RECIPE_CATEGORY, id, 125, 18);
        this.inputs.add(EmiIngredient.of(inputs));
        this.outputs.add(EmiStack.of(output));
        this.time = timeInMinutes;
        List<EmiStack> list = new ArrayList<>();
        byproducts.forEach(i -> list.add(EmiStack.of(i)));
        this.byproducts = EmiIngredient.of(list);
        this.outputs.addAll(list);
    }
    private final  int time; // it needs time.
    private final EmiIngredient byproducts;

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
        widgetHolder.addSlot(this.byproducts, 80, 0).recipeContext(this);
    }
}
