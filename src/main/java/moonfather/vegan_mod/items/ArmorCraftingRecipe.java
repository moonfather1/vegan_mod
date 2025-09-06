package moonfather.vegan_mod.items;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ArmorCraftingRecipe extends CustomRecipe {
    public ArmorCraftingRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level)
    {
        if (craftingInput.width() != 3 || craftingInput.height() < 2 || craftingInput.height() > 3)  return false;
        if (! craftingInput.getItem(0).isEmpty() && ! craftingInput.getItem(0).is(VeganMod.Items.HARDENED_FABRIC)) return false; // quick check
        return getBaseResult(craftingInput) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider)
    {
        if (craftingInput.width() != 3 || craftingInput.height() < 2 || craftingInput.height() > 3)  return ItemStack.EMPTY;
        Item resultItem = getBaseResult(craftingInput);
        if (resultItem == null)
        {
            return ItemStack.EMPTY; // should never happen
        }
        ItemStack result = new ItemStack(resultItem);
        result.set(DataComponents.LORE, new ItemLore(List.of(lore_line)));
        result.set(DataComponents.DYED_COLOR, new DyedItemColor(armor_color, false));
        result.set(VeganMod.Other.VEGAN_MARKER, Unit.INSTANCE);
        return result;
    }

    private Item getBaseResult(CraftingInput craftingInput)
    {
        int max = craftingInput.width() * craftingInput.height(); // 9 or 6
        boolean match = true; // assume armor1
        for (int i = 0; i < max; i++)
        {
            if (i == 1 && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (i != 1 && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max == 9) return Items.LEATHER_CHESTPLATE; // armor1
        match = true; // assume armor2
        for (int i = 0; i < max; i++)
        {
            if ((i == 4 || i == 7) && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (! (i == 4 || i == 7) && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max == 9) return Items.LEATHER_LEGGINGS; // armor2
        match = true; // assume armor3
        for (int i = 0; i < max; i++)
        {
            if ((i == 1 || i == 4 || i > 5) && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (! (i == 1 || i == 4 || i > 5) && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max > 3) return Items.LEATHER_BOOTS; // armor3
        match = true; // assume armor4
        for (int i = 0; i < max; i++)
        {
            if ((i == 4 || i > 5) && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (! (i == 4 || i > 5) && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max > 3) return Items.LEATHER_HELMET; // armor4
        match = true; // assume armor3
        for (int i = 0; i < max; i++)
        {
            if ((i < 3 || i == 4 || i == 7) && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (! (i < 3 || i == 4 || i == 7) && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max > 3) return Items.LEATHER_BOOTS; // armor3
        match = true; // assume armor4
        for (int i = 0; i < max; i++)
        {
            if ((i < 3 || i == 7) && ! craftingInput.getItem(i).isEmpty()) match = false;
            if (! (i < 3 || i == 7) && ! craftingInput.getItem(i).is(VeganMod.Items.HARDENED_FABRIC)) match = false;
        }
        if (match && max > 3) return Items.LEATHER_HELMET; // armor4
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j)
    {
        return i >= 3 && j >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return serializer;
    }
    public static RecipeSerializer<?> getSerializerForRegistration()
    {
        return serializer;
    }
    private static final RecipeSerializer<ArmorCraftingRecipe> serializer = new SimpleCraftingRecipeSerializer<ArmorCraftingRecipe>(ArmorCraftingRecipe::new);
    private static final Component lore_line = Component.translatable("message.vegan_mod.armor_subtitle").withColor(0x339911);
    private static final int armor_color = 0x339911;
}