package moonfather.vegan_mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import org.jetbrains.annotations.Nullable;

public record TagNotEmptyRecipeCondition(String tag_id) implements ResourceCondition
{
    public static final MapCodec<TagNotEmptyRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("tag_id").orElse("missing").forGetter(TagNotEmptyRecipeCondition::tag_id)
    ).apply(instance, TagNotEmptyRecipeCondition::new));

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
    public boolean test(@Nullable RegistryOps.RegistryInfoLookup registryInfoLookup)
    {
        if (this.tag_id == null) return false;
        int count = TagConditionSupport.INSTANCE.getCount(Identifier.parse(this.tag_id));
        return count > 0;
    }
}
