package moonfather.vegan_mod.integration;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RrvRecipeDrying implements ReliableClientRecipe
{
    private final Identifier id;
    private final SlotContent baseItem, resultItem;
    private final int time;

    public RrvRecipeDrying(Ingredient baseItem, ItemStack resultItem, int timeInMinutes, Identifier id)
    {
        this.id = id;
        this.baseItem = SlotContent.of(baseItem);
        this.resultItem = SlotContent.of(resultItem);
        this.time = timeInMinutes;
    }

    @Override
    public ReliableClientRecipeType getType() { return DryingClientRecipeType.INSTANCE; }
    @Override
    public Identifier getId() { return this.id; }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext)
    {
        slotFillContext.bindSlot(0, this.baseItem);
        slotFillContext.bindSlot(1, this.resultItem);
        slotFillContext.addAdditionalStackModifier(1, (stack, tooltip) -> {
            tooltip.add(Component.translatable("emi.category.vegan_mod.time", this.time));
        });
    }

    @Override
    public List<SlotContent> getIngredients()
    {
        return List.of(this.baseItem);
    }

    @Override
    public List<SlotContent> getResults()
    {
        return List.of(this.resultItem);
    }

    /// /////////////////////////////////////////////////////////////

    public static class DryingClientRecipeType implements ReliableClientRecipeType
    {
        protected static final ReliableClientRecipeType INSTANCE = new DryingClientRecipeType();


        @Override
        public Component getDisplayName()
        {
            return Component.translatable("emi.category.vegan_mod.emi_category1");
        }

        @Override
        public int getDisplayWidth()
        {
            return 125; //The width of your type's gui texture
        }

        @Override
        public int getDisplayHeight()
        {
            return 52; //The height of your type's gui texture
        }

        @Override
        public @Nullable Identifier getGuiTexture()
        {
            return Identifier.fromNamespaceAndPath(VeganMod.MODID, "textures/gui/rrv_1.png");
        }

        @Override
        public int getSlotCount()
        {
            return 2;
        }

        @Override
        public void placeSlots(RecipeViewMenu.SlotDefinition slotDefinition)
        {
            //Tell RRV where your slots are located by calling slotDefinition.addItemSlot();
            //NOTE: Slot position is relative to your gui texture
            slotDefinition.addItemSlot(0,  9, 12);
            slotDefinition.addItemSlot(1, 50, 12);
        }

        @Override
        public Identifier getId()
        {
            return Identifier.fromNamespaceAndPath(VeganMod.MODID, "recipe_type"); // The unique id of this recipe type
        }

        @Override
        public ItemStack getIcon()
        {
            return VeganMod.Blocks.DRYING_RACK_ITEM.get().getDefaultInstance(); //The icon displayed in the recipe view screen
        }

        @Override
        public List<ItemStack> getCraftReferences()
        {
            return List.of(VeganMod.Blocks.DRYING_RACK_ITEM.get().getDefaultInstance()); //Return a list of blocks/items that can be used to process your recipes (e.g. for Smelting it would be the Furnace)
        }
    }
}