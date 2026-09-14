package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.blocks.KilnBlock;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeServerPlugin implements IWailaPlugin
{
    @Override
    public void register(IWailaCommonRegistration registration)
    {
        registration.registerBlockDataProvider(JadeKilnDataProvider.getInstance(), KilnBlock.class);
    }
}