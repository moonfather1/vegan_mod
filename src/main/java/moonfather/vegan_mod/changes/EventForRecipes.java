package moonfather.vegan_mod.changes;

import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EventBusSubscriber
public class EventForRecipes
{
    @SubscribeEvent
    public static void OnServerStarting(ServerStartingEvent event)
    {
        RecipeManagerMain.beforeSync(event.getServer());
    }

    //-----------------------------------

    @SubscribeEvent
    public static void OnAddReloadListener(AddReloadListenerEvent event)
    {
        for (PreparableReloadListener listener: event.getListeners())
        {
            if (listener instanceof ReloadListener)
            {
                return;
            }
        }
        event.addListener(lissy);
    }

    private static final PreparableReloadListener lissy = new ReloadListener();

    private static class ReloadListener implements PreparableReloadListener
    {
        @Override
        public CompletableFuture<Void> reload(PreparationBarrier p_10638_, ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_)
        {
            if (ServerLifecycleHooks.getCurrentServer() != null) // will be null one during load
            {
                RecipeManagerMain.beforeSync(ServerLifecycleHooks.getCurrentServer());
            }
            return p_10638_.wait(null);
        }
    }
}
