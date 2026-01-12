package moonfather.vegan_mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.ICondition;

public record OptionalRecipeCondition(String flag) implements ICondition
{
    @Override
    public boolean test(IContext iContext)
    {
        if (flag == null) { return false; }
        if (flag.equals("make_leather_on_crafting_table")) { return Config.leather_make_on_crafting_table(); }
        if (flag.equals("make_leather_on_drying_rack")) { return Config.leather_make_on_drying_rack(); }
        if (flag.equals("ink_accepts_blue_dye")) { return Config.ink_accepts_blue_dye(); }
        if (flag.equals("oil_bucket_forced_craftable")) { return Config.oil_bucket_always_craftable(); }
        if (flag.equals("ink_simple_recipe")) { return false; }
        return false;
    }

    /////////////////////////////////////////////////////

    @Override
    public MapCodec<? extends ICondition> codec()
    {
        return CODEC;
    }

    public static MapCodec<OptionalRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Codec.STRING.fieldOf("flag").forGetter(orc -> orc.flag))
                    .apply(builder, OptionalRecipeCondition::new));
}
