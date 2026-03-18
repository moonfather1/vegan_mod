package moonfather.vegan_mod.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

public class DataMapManager
{
    public static record DryingRecipe(Holder<Item> output, int timeInMinutes)
    {
        public static final Codec<DryingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.ITEM_NON_AIR_CODEC.fieldOf("output").forGetter(DryingRecipe::output),
                Codec.intRange(1, 120).fieldOf("time_in_minutes").forGetter(DryingRecipe::timeInMinutes)
        ).apply(instance, DryingRecipe::new));
    }

    public static DryingRecipe getRecipe(ItemStack stack)
    {
        Holder<Item> holder = stack.getItemHolder();
        return holder.getData(DRYING_RECIPE); // null if not in data map
    }
    /////////////////////////////////////////////////////

    public static final DataMapType<Item, DryingRecipe> DRYING_RECIPE = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(VeganMod.MODID, "drying_on_rack"),
            Registries.ITEM,
            DryingRecipe.CODEC
    ).build();



    public static void registerDataMapTypes(RegisterDataMapTypesEvent event)
    {
        event.register(DRYING_RECIPE);
    }
}
