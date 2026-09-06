package moonfather.vegan_mod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record TagEmptyRecipeCondition(String tag_id) implements ResourceCondition
{
    public static final MapCodec<TagEmptyRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("tag_id").orElse("missing").forGetter(TagEmptyRecipeCondition::tag_id)
    ).apply(instance, TagEmptyRecipeCondition::new));

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
        if (this.tag_id == null) return false;
        int count = TagConditionSupport.INSTANCE.getCount(ResourceLocation.parse(this.tag_id));
        return count == 0;
    }
}
