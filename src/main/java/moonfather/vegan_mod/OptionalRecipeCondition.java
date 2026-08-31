package moonfather.vegan_mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;

public record OptionalRecipeCondition(String flag) implements ResourceCondition
{
    public static final MapCodec<OptionalRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("flag").orElse("missing").forGetter(OptionalRecipeCondition::flag)
    ).apply(instance, OptionalRecipeCondition::new));

    @Override
    public ResourceConditionType<?> getType()
    {
        return type;
    }
    public static void setType(ResourceConditionType<?> registeredType)
    {
        type = registeredType;
    }
    private static ResourceConditionType<?> type = null;

    @Override
    public boolean test(HolderLookup.@Nullable Provider registryLookup)
    {
        if (flag == null) { return false; }
        if (flag.equals("make_leather_on_crafting_table")) { return OptionsCommon.leather_make_on_crafting_table(); }
        if (flag.equals("make_leather_on_drying_rack")) { return OptionsCommon.leather_make_on_drying_rack(); }
        if (flag.equals("ink_accepts_blue_dye")) { return OptionsCommon.ink_accepts_blue_dye(); }
        //if (flag.equals("oil_bucket_forced_craftable")) { return OptionsCommon.oil_bucket_always_craftable(); }
        //if (flag.equals("charcoal_kiln_enabled")) { return OptionsCommon.kiln_enabled(); }
        //if (flag.equals("charcoal_kiln_disabled")) { return ! OptionsCommon.kiln_enabled(); }
        return false;
    }
}
