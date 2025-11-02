package moonfather.vegan_mod.stupid_fluid;

import moonfather.vegan_mod.VeganMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = VeganMod.MODID, value = Dist.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void onClientSetup(RegisterClientExtensionsEvent event)
    {
        IClientFluidTypeExtensions ex = ClientExtensions.create(VeganMod.MODID, "plant_oil").tint(0xCCAA66).fogColor(0.6f, 0.6f, 0.3f).build();
        event.registerFluidType(ex, FluidRegistration.OIL_FLUID.type.get());
    }
}
