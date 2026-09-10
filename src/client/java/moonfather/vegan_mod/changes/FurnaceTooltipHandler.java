package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.blocks.KilnMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FurnaceTooltipHandler
{
    public static void onTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> components)
    {
        if (Config.kiln_enabled())
        {
            if (itemStack.is(ItemTags.LOGS_THAT_BURN))
            {
                if (Minecraft.getInstance().player != null)
                {
                    if (Minecraft.getInstance().player.containerMenu instanceof AbstractFurnaceMenu)
                    {
                        if (info.isEmpty())
                        {
                            Arrays.stream(Language.getInstance().getOrDefault(TEXT_KEY)
                                            .split("\n"))
                                    .forEach(text -> info.add(Component.literal(text).withStyle(Style.EMPTY.withColor(0xffddbb66))));
                        }
                        components.addAll(info);
                    }
                    if (Minecraft.getInstance().player.containerMenu instanceof KilnMenu)
                    {
                        if (info2.isEmpty())
                        {
                            Arrays.stream(Language.getInstance().getOrDefault(TEXT_KEY_2)
                                            .split("\n"))
                                    .forEach(text -> info2.add(Component.literal(text).withStyle(Style.EMPTY.withColor(0xffbbbbcc))));
                        }
                        components.addAll(info2);
                    }
                }
            }
        }
    }

    private static final List<Component> info = new ArrayList<>(6);
    private static final String TEXT_KEY = "message.vegan_mod.logs_in_furnace";
    private static final List<Component> info2 = new ArrayList<>(4);
    private static final String TEXT_KEY_2 = "message.vegan_mod.logs_in_kiln";

}
