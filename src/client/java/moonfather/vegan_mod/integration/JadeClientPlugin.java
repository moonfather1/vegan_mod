package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.blocks.KilnBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.impl.WailaClientRegistration;

@WailaPlugin
public class JadeClientPlugin implements IWailaPlugin
{
    @Override
    public void registerClient(IWailaClientRegistration registration)
    {
        registration.registerBlockComponent(JadeKilnTooltipProvider.getInstance(), KilnBlock.class);
    }

    public static void registerClientManuallyBecauseThingsAreStupid()
    {
        WailaClientRegistration.instance().registerBlockComponent(JadeKilnTooltipProvider.getInstance(), KilnBlock.class);
    }
}