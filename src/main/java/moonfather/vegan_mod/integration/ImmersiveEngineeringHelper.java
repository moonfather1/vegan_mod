package moonfather.vegan_mod.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

public class ImmersiveEngineeringHelper
{
    public static boolean loaded()
    {
        return ModList.get().isLoaded("immersiveengineering");
    }

    public static Item getCoalItem()
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse("immersiveengineering:coal_coke"));
    }

    public static Item getBucketItem()
    {
        if (loaded())
        {
            return BuiltInRegistries.ITEM.get(ResourceLocation.parse("immersiveengineering:creosote_bucket"));
        }
        else
        {
            return Items.STICK;
        }
    }
}
