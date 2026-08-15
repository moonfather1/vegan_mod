package moonfather.vegan_mod;

import moonfather.vegan_mod.blocks.client_side.KilnScreen;
import moonfather.vegan_mod.blocks.DryingRackBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = VeganMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = VeganMod.MODID, value = Dist.CLIENT)
public class VeganModClient
{
    public VeganModClient(ModContainer container)
    {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
    }

    @SubscribeEvent
    public static void RegisterScreens(RegisterMenuScreensEvent event)
    {
        event.register(VeganMod.BlockEntities.KILN_MENU_TYPE.get(), KilnScreen::new);
    }

    @SubscribeEvent
    public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(VeganMod.BlockEntities.DRYING_RACK_BE.get(), DryingRackBlockEntityRenderer::new);
    }
}
