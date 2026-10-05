package moonfather.vegan_mod;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import moonfather.vegan_mod.blocks.DryingRackBlockEntityRenderer;
import moonfather.vegan_mod.blocks.KilnScreen;
import moonfather.vegan_mod.changes.FurnaceTooltipHandler;
import moonfather.vegan_mod.integration.JadeProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class ClientEntryPoint implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ConfigScreenFactoryRegistry.INSTANCE.register(VeganMod.MOD_ID, ConfigurationScreen::new);
        ItemTooltipCallback.EVENT.register(FurnaceTooltipHandler::onTooltip);
        BlockEntityRenderers.register(VeganMod.Blocks.DRYING_RACK_BLOCK_ENTITY, DryingRackBlockEntityRenderer::new);

        MenuScreens.register(VeganMod.Blocks.KILN_MENU_TYPE, KilnScreen::new);

//        if (FabricLoader.getInstance().isModLoaded("jade"))
//        {
//            //JadeProxy.registerPluginInAStupidAndConvolutedManner();
//            ClientLifecycleEvents.CLIENT_STARTED.register(JadeProxy::registerPluginInAStupidAndConvolutedManner);
//        }  WELL NO CAN DO. STUPID JADE RESETS COLLECTIONS AT THE TOP OF ITS loadPlugin LOCKS THEM AFTERWARDS.
    }
}