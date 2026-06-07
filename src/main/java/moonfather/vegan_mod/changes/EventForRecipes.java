package moonfather.vegan_mod.changes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


@EventBusSubscriber
public class EventForRecipes
{
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event)
    {
        RecipeManagerMain.beforeSync(event.getServer().getRecipeManager(), event.getServer().overworld().registryAccess());
    }

    //-----------------------------------

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event)
    {
        // i checked, there won't be an existing on in collection.
        event.addListener(new ReloadListener(event.getServerResources().getRecipeManager(), event.getRegistryAccess()));
    }



    private static class ReloadListener implements ResourceManagerReloadListener //PreparableReloadListener
    {
        private ReloadListener(RecipeManager recipeManager, RegistryAccess registryAccess)
        {
            this.recipeManager = recipeManager;
            this.registryAccess = registryAccess;
        }
        private final RecipeManager recipeManager;  private final RegistryAccess registryAccess;

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager)
        {
            if (ServerLifecycleHooks.getCurrentServer() != null)
            {
                RecipeManagerMain.beforeSync(this.recipeManager, this.registryAccess);
            }
        }
    }
}
