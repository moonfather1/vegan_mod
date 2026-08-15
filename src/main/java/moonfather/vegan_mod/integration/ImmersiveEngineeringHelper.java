package moonfather.vegan_mod.integration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
        var ohr =  BuiltInRegistries.ITEM.get(Identifier.parse("immersiveengineering:coal_coke"));
        return ohr.map(Holder.Reference::value).orElse(Items.COAL);
    }

    public static Item getBucketItem()
    {
        if (loaded())
        {
            var ohr = BuiltInRegistries.ITEM.get(Identifier.parse("immersiveengineering:creosote_bucket"));
            return ohr.map(Holder.Reference::value).orElse(Items.BUCKET);
        }
        else
        {
            return Items.STICK;
        }
    }
}
