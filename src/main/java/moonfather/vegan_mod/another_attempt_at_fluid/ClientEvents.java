package moonfather.vegan_mod.another_attempt_at_fluid;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber
public class ClientEvents
{
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event)
    {
        event.registerFluidType(
                new IClientFluidTypeExtensions()
                {
                    final Identifier FLOOD_STILL = Identifier.withDefaultNamespace("block/water_still");
                    final Identifier FLOOD_FLOW = Identifier.withDefaultNamespace("block/water_flow");
                    final Identifier FLOOD_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
                    final Identifier FLOOD_LOCATION = Identifier.withDefaultNamespace("textures/misc/underwater.png");
                    final Identifier FLOOD_LOCATION2 = Identifier.withDefaultNamespace("misc/underwater");

                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft)
                    {
                        return FLOOD_LOCATION2;
                    }

                },
                FluidRegistries.OIL_TYPE
        );
    }



    @SubscribeEvent
    public static void onRegisterFluidModels(RegisterFluidModelsEvent event)
    {
        final Identifier FLOOD_STILL = Identifier.withDefaultNamespace("block/water_still");
        final Identifier FLOOD_FLOW = Identifier.withDefaultNamespace("block/water_flow");
        final Identifier FLOOD_OVERLAY = Identifier.withDefaultNamespace("block/water_overlay");
        event.register(
                new FluidModel.Unbaked(
                        new Material(FLOOD_STILL),
                        new Material(FLOOD_FLOW),
                        // Overlay texture is optional (and apparently ignored)
                        new Material(FLOOD_OVERLAY),
                        // So is FluidTintSource. You can leave this out if your texture already has color
                        _->0XFFD1C879
                ),
                FluidRegistries.OIL_SOURCE.get(),
                FluidRegistries.OIL_FLOWING.get()
        );
    }
}
