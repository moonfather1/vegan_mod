package moonfather.vegan_mod.integration;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class RrvRecipeCharcoal implements ReliableClientRecipe
{
    private final  SlotContent in1, out1, out2, out3;

    public RrvRecipeCharcoal()
    {
        this.in1 = SlotContent.of(ItemTags.LOGS_THAT_BURN);
        this.out1 = SlotContent.of(Items.CHARCOAL);
        ItemStack bottle = VeganMod.Items.THICK_OIL.getDefaultInstance();
        bottle.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.thick_oil2"));
        this.out2 = SlotContent.of(bottle);
        if (Config.kiln_gives_tar_paint())
            this.out3 = SlotContent.of(KilnBlockEntity.makeTarItemStack(1));
        else
            this.out3 = SlotContent.of();
    }

    @Override
    public ReliableClientRecipeType getType() { return CharcoalClientRecipeType.INSTANCE; }
    @Override
    public Identifier getId() { return Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, "/kiln1"); }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext)
    {
        slotFillContext.bindSlot(0, in1);
        slotFillContext.bindSlot(1, out1);
        slotFillContext.bindSlot(2, out2);
        slotFillContext.bindOptionalSlot(3, out3, RecipeViewMenu.OptionalSlotRenderer.DEFAULT);

        slotFillContext.addAdditionalStackModifier(1, (stack, tooltip) -> {
            Arrays.stream(Language.getInstance().getOrDefault("message.vegan_mod.logs_in_kiln")
                            .split("\n"))
                    .forEach(text -> tooltip.add(Component.literal(text).withStyle(Style.EMPTY.withColor(0xffbbbbcc))));
        });
    }

    @Override
    public List<SlotContent> getIngredients()
    {
        return List.of(in1);
    }

    @Override
    public List<SlotContent> getResults()
    {
        if (Config.kiln_gives_tar_paint())
            return List.of(out1, out2, out3);
        else
            return List.of(out1, out2);
    }

    /// /////////////////////////////////////////////////////////////

    public static class CharcoalClientRecipeType implements ReliableClientRecipeType
    {
        protected static final ReliableClientRecipeType INSTANCE = new CharcoalClientRecipeType();


        @Override
        public Component getDisplayName()
        {
            return Component.translatable("emi.category.vegan_mod.emi_category2");
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
            return Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, "textures/gui/rrv_2.png");
        }

        @Override
        public int getSlotCount()
        {
            return 4;
        }

        @Override
        public void placeSlots(RecipeViewMenu.SlotDefinition slotDefinition)
        {
            //Tell RRV where your slots are located by calling slotDefinition.addItemSlot();
            //NOTE: Slot position is relative to your gui texture
            slotDefinition.addItemSlot(0,   9, 12);
            slotDefinition.addItemSlot(1,  50, 12);
            slotDefinition.addItemSlot(2,  75, 12);
            slotDefinition.addItemSlot(3, 100, 12);
        }

        @Override
        public Identifier getId()
        {
            return Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, "recipe_type_2"); // The unique id of this recipe type
        }

        @Override
        public ItemStack getIcon()
        {
            return Items.CHARCOAL.getDefaultInstance(); //The icon displayed in the recipe view screen
        }

        @Override
        public List<ItemStack> getCraftReferences()
        {
            return List.of(VeganMod.Blocks.KILN_ITEM.getDefaultInstance()); //Return a list of blocks/items that can be used to process your recipes (e.g. for Smelting it would be the Furnace)
        }
    }
}
