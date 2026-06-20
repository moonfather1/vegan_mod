package moonfather.vegan_mod.items;

import moonfather.vegan_mod.Config;
import net.minecraft.network.chat.Component;

public abstract class ArmorManagement
{
    public static int getArmorColor()
    {
        if (armor_color == 0)
        {
            try
            {
                armor_color = Integer.decode(Config.leather_armor_color());
            }
            catch (NumberFormatException ex)
            {
                armor_color = armor_color_default;
            }
        }
        return armor_color;
    }



    private static final Component lore_line = Component.translatable("message.vegan_mod.armor_subtitle").withColor(0x339911);
    private static final int armor_color_default = 0x339911;
    private static int armor_color = 0;



    public static Component getLore()
    {
        return lore_line;
    }
}
