package moonfather.vegan_mod;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.*;

public class TagConditionSupport
{
    public static final TagConditionSupport INSTANCE = new TagConditionSupport();

    private final Map<TagKey<?>, List<? extends Holder<?>>> pendingContents;
    private Map<Identifier, Integer> cache = new HashMap<>();

    private TagConditionSupport()
    {
        this.pendingContents = new IdentityHashMap<>();
    }
    public void setTagCollection(List<Registry.PendingTags<?>> pendingTags)
    {
        this.pendingContents.clear();
        for (Registry.PendingTags<?> tags : pendingTags)
        {
            //this.pendingContents.putAll(tags.contents());
        }
        cache.clear();
    }

    public int getCount(Identifier id)
    {
        if (cache.containsKey(id))
        {
            return cache.get(id);
        }
        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, id);
//        List<? extends Holder<?>> contents = this.pendingContents.get(tagKey);
//        int result =  contents != null ? contents.size() : 0;
        int result = BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).iterator().hasNext() ? 2 : 0;


        cache.put(id, result);
        return result;
    }
}
