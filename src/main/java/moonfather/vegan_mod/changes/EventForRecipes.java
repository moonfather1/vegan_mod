package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


@EventBusSubscriber
public class EventForRecipes
{
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event)
    {
        RecipeManagerMain.beforeSync(event.getServer().getRecipeManager());
    }

    //-----------------------------------

    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event)
    {
        // i checked, there won't be an existing on in collection.
        event.addListener(id, new ReloadListener(event.getServerResources().getRecipeManager()));
    }



    private static class ReloadListener implements ResourceManagerReloadListener //PreparableReloadListener
    {
        private ReloadListener(RecipeManager recipeManager)
        {
            this.recipeManager = recipeManager;
        }
        private final RecipeManager recipeManager;

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager)
        {
            if (ServerLifecycleHooks.getCurrentServer() != null)
            {
                RecipeManagerMain.beforeSync(this.recipeManager);
            }
        }
    }
    private static final Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MODID, "recipe_listener");
}
