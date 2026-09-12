package moonfather.vegan_mod.items;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class IdentifiableBlockItem extends BlockItem
{
    public IdentifiableBlockItem(Block block, String shortId)
    {
        Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, shortId);
        super(block, new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
        this.mainId = id;
    }
    private final Identifier mainId;



    public Identifier getMainId() { return mainId; }
}
