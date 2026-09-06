package moonfather.vegan_mod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagManager;

import java.util.*;

public class TagConditionSupport
{
    public static final TagConditionSupport INSTANCE = new TagConditionSupport();

    private TagManager tagManager = null;
    private Map<ResourceLocation, Integer> cache = new HashMap<>();

    private TagConditionSupport()
    {
    }
    public void  setTagManager(TagManager tagManager)
    {
        this.tagManager = tagManager;
    }

    public int getCount(ResourceLocation tagKey)
    {
        if (cache.containsKey(tagKey))
        {
            return cache.get(tagKey);
        }
        var tags = tagManager.getResult();
        if (tags.isEmpty()) throw new IllegalStateException("Tags have not been loaded yet.");
        for (var loadResult : tags)
        {
            if (loadResult.key().equals(Registries.ITEM))
            {
                var itemList = loadResult.tags().get(tagKey);
                int count = itemList != null ? itemList.size() : 0;
                cache.put(tagKey, count);
                return count;
            }
        }
        return 0;
    }
}
