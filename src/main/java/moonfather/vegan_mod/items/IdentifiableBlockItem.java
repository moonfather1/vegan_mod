package moonfather.vegan_mod.items;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.function.Function;

public class IdentifiableBlockItem extends BlockItem
{
    public IdentifiableBlockItem(Block block, String shortId)
    {
        Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, shortId);
        super(block, new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
        this.mainId = id;
    }
    public IdentifiableBlockItem(Block block, String shortId, Function<Properties, Properties> propertiesAppender)
    {
        Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MOD_ID, shortId);
        Properties p = propertiesAppender.apply(new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
        super(block, p);
        this.mainId = id;
    }
    private final Identifier mainId;



    public Identifier getMainId() { return mainId; }
}
