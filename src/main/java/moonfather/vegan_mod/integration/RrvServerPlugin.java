package moonfather.vegan_mod.integration;

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.common.recipe.ServerRecipeManager;
import moonfather.vegan_mod.VeganMod;

public class RrvServerPlugin implements ReliableRecipeViewerPlugin
{
    @Override
    public void onIntegrationInitialize()
    {
        // Here, you can tell the server to synchronize your mod's recipes to the client. In this example, we're using the server recipe manager to synchronize recipes for an example mod's upgrading recipe type.
        ServerRecipeManager.INSTANCE.synchronizeRecipeType(VeganMod.Other.DRYING_RECIPE_SERIALIZER, VeganMod.Other.DRYING_RECIPE_TYPE);
    }
}
