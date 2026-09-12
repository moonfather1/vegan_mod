package moonfather.vegan_mod.items;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class IdentifiableItem extends Item
{
    public IdentifiableItem(String shortId)
    {
        Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, shortId);
        super(new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
        this.mainId = id;
    }
    private final Identifier mainId;



    public Identifier getMainId() { return mainId; }
}
