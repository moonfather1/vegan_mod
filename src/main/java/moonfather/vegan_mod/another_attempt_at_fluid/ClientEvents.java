package moonfather.vegan_mod.another_attempt_at_fluid;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.ParametersAreNonnullByDefault;

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

//                    @Override
//                    public Identifier getStillTexture() {
//                        return FLOOD_STILL;
//                    }
//
//                    @Override
//                    public Identifier getFlowingTexture()
//                    {
//                        return FLOOD_FLOW;
//                    }
//
//                    @Override
//                    public Identifier getOverlayTexture()
//                    {
//                        return FLOOD_OVERLAY;
//                    }

                    @Override
                    public Identifier getRenderOverlayTexture(Minecraft minecraft)
                    {
                        return FLOOD_LOCATION;
                    }

//                    @Override
//                    public int getTintColor()
//                    {
//                        // Taken from net.neoforged.neoforge.client.ClientNeoForgedMod water_type
//                        return 0xFFE6DA9E;
//                    }
//
//                    @Override
//                    @ParametersAreNonnullByDefault
//                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos)
//                    {
//                        //return  FastColor.ARGB32.color(0xFF, 0xE6, 0xDA, 0x9E);
//                        return  0xFFE6DA9E;
//                    }
                },
                FluidRegistries.OIL_TYPE
        );
    }



//    @SubscribeEvent
//    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event)
//    {
//        if (colors == null)
//        {
//            colors = event.getItemColors();
//        }
//        event.register((stack, tintIndex) ->
//                {
//                    if (tintIndex == 1)
//                    {
//                        return 0XFFD1C879;
//                    }
//                    else
//                    {
//                        if (defaultColor == -308)
//                        {
//                            defaultColor = colors.getColor(new ItemStack(Items.BUCKET), 0);
//                        }
//                        return defaultColor;
//                    }
//                },
//                FluidRegistries.OIL_BUCKET.get()
//        );
//    }
//    private static int defaultColor = -308;              12345637
//    private static ItemColors colors = null;



//    @SubscribeEvent
//    static void onClientSetup(FMLClientSetupEvent event)
//    {
//        // ItemBlockRenderTypes.setRenderLayer is deprecated for regular blocks, and specified in their json files instead.
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.OIL_SOURCE.get(), RenderType.translucent());
//        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.OIL_FLOWING.get(), RenderType.translucent());
//
//    }
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
