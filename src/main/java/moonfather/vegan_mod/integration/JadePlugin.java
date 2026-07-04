package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.blocks.KilnBlock;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin
{
    @Override
    public void registerClient(IWailaClientRegistration registration)
    {
        registration.registerBlockComponent(JadeKilnTooltipProvider.getInstance(), KilnBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration)
    {
        registration.registerBlockDataProvider(JadeKilnTooltipProvider.getInstance(), KilnBlockEntity.class);
    }
}
