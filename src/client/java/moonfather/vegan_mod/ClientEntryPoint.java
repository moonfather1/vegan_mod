package moonfather.vegan_mod;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import moonfather.vegan_mod.blocks.DryingRackBlockEntityRenderer;
import moonfather.vegan_mod.changes.FurnaceTooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
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
    }
}