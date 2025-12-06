package moonfather.vegan_mod.another_attempt_at_fluid;


import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
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
                    final ResourceLocation FLOOD_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
                    final ResourceLocation FLOOD_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");
                    final ResourceLocation FLOOD_OVERLAY = ResourceLocation.withDefaultNamespace("block/water_overlay");
                    final ResourceLocation FLOOD_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");

                    @Override
                    public ResourceLocation getStillTexture() {
                        return FLOOD_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture()
                    {
                        return FLOOD_FLOW;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture()
                    {
                        return FLOOD_OVERLAY;
                    }

                    @Override
                    public ResourceLocation getRenderOverlayTexture(Minecraft minecraft)
                    {
                        return FLOOD_LOCATION;
                    }

                    @Override
                    public int getTintColor()
                    {
                        // Taken from net.neoforged.neoforge.client.ClientNeoForgedMod water_type
                        return 0xFFE6DA9E;
                    }

                    @Override
                    @ParametersAreNonnullByDefault
                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos)
                    {
                        return  FastColor.ARGB32.color(0xFF, 0xE6, 0xDA, 0x9E);
                    }
                },
                FluidRegistries.OIL_TYPE
        );
    }



    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event)
    {
        if (colors == null)
        {
            colors = event.getItemColors();
        }
        event.register((stack, tintIndex) ->
                {
                    if (tintIndex == 1)
                    {
                        return 0XFFD1C879;
                    } else {
                        if (defaultColor == -308)
                        {

                            defaultColor = colors.getColor(new ItemStack(Items.BUCKET), 0);
                        }
                        return defaultColor;
                    }
                },
                FluidRegistries.OIL_BUCKET.get()
        );
    }
    private static int defaultColor = -308;
    private static ItemColors colors = null;



    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event)
    {
        // ItemBlockRenderTypes.setRenderLayer is deprecated for regular blocks, and specified in their json files instead.
        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.OIL_SOURCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FluidRegistries.OIL_FLOWING.get(), RenderType.translucent());

    }
}
