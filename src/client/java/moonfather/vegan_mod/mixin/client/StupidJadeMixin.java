package moonfather.vegan_mod.mixin.client;

import moonfather.vegan_mod.integration.JadeProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.Jade;

@Pseudo
@Mixin(Jade.class)
public class StupidJadeMixin
{
    @Inject(at = @At(value = "INVOKE", target = "Lsnownee/jade/impl/WailaClientRegistration;reset()V", shift = At.Shift.AFTER), method = "Lsnownee/jade/Jade;loadPlugins(Ljava/util/List;Ljava/util/Set;Ljava/util/Set;Ljava/util/Set;)V")
    private static void init(CallbackInfo info)
    {
        // we can't do on HEAD or usual ClientLifecycleEvents.CLIENT_STARTED because it does a reset at start of loadPlugins() and a bit later locks up and turns some map immutable.  argh.
        JadeProxy.registerPluginInAStupidAndConvolutedManner(null);
    }
}