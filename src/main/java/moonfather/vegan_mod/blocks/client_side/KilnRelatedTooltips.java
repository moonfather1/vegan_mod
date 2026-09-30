package moonfather.vegan_mod.blocks.client_side;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.blocks.KilnMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber
public class KilnRelatedTooltips
{
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        if (Config.kiln_enabled())
        {
            if (event.getItemStack().is(ItemTags.LOGS_THAT_BURN))
            {
                if (event.getEntity() != null && event.getEntity().containerMenu instanceof AbstractFurnaceMenu)
                {
                    event.getToolTip().add(info1);
                }
                if (event.getEntity() != null && event.getEntity().containerMenu instanceof KilnMenu)
                {
                    event.getToolTip().add(info2);
                }
            }
        }
    }
    private static final Component info1 = Component.translatable("message.vegan_mod.logs_in_furnace").withColor(0xffddbb66);
    private static final Component info2 = Component.translatable("message.vegan_mod.logs_in_kiln").withColor(0xffddbb66);
}