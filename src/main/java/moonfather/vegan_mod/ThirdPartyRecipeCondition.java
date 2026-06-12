package moonfather.vegan_mod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public record ThirdPartyRecipeCondition(String targetType, String targetContent, boolean valueIfModMotPresent) implements ICondition
{
    @Override
    public boolean test(IContext iContext)
    {
        Identifier conditionId = Identifier.parse(this.targetType);
        if (NeoForgeRegistries.CONDITION_SERIALIZERS.containsKey(conditionId))
        {
            Optional<Holder.Reference<MapCodec<? extends ICondition>>> op = NeoForgeRegistries.CONDITION_SERIALIZERS.get(conditionId);
            if (op.isPresent())
            {
                MapCodec<? extends ICondition> codec = op.get().value();
                JsonObject jsonObject = JsonParser.parseString(this.targetContent).getAsJsonObject();
                var result = codec.codec().decode(JsonOps.INSTANCE, jsonObject);
                if (result.isSuccess())
                {
                    return result.getOrThrow().getFirst().test(iContext);
                }
            }
        }
        return this.valueIfModMotPresent;
    }

    /////////////////////////////////////////////////////

    @Override
    public MapCodec<? extends ICondition> codec()
    {
        return CODEC;
    }

    public static MapCodec<ThirdPartyRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Codec.STRING.fieldOf("target_type").forGetter(tprc -> tprc.targetType),
                            Codec.STRING.fieldOf("target_content").forGetter(tprc -> tprc.targetContent),
                            Codec.BOOL.fieldOf("value_if_not_present").forGetter(tprc -> tprc.valueIfModMotPresent))
                    .apply(builder, ThirdPartyRecipeCondition::new));
}
