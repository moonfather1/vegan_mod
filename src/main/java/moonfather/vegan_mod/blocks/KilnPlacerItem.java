package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

public class KilnPlacerItem extends Item
{
    private final Block block;

    public KilnPlacerItem(Block block, String id)
    {
        super(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(VeganMod.MODID, id))).useBlockDescriptionPrefix());
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        BlockPlaceContext context2 = new BlockPlaceContext(context);
        if (! context2.canPlace()) { return InteractionResult.FAIL; }
        if (context.getLevel().isClientSide()) { return  InteractionResult.SUCCESS; }
        BlockPos target = context.getClickedPos();
        BlockState existing1 = context.getLevel().getBlockState(target);
        if (! existing1.canBeReplaced(context2))
        {
            target = context.getClickedPos().relative(context.getClickedFace());
            existing1 = context.getLevel().getBlockState(target);
        }
        BlockState existing2 = context.getLevel().getBlockState(target.above());
        if (! existing1.canBeReplaced() || ! existing2.canBeReplaced())
        {
            if (context.getPlayer() != null)
            {
                context.getPlayer().sendOverlayMessage(Component.translatable("message.vegan_mod.no_room_for_kiln_1"));
            }
            return InteractionResult.FAIL;
        }
        BlockState toPlace = this.block.defaultBlockState()
                .setValue(KilnBlock.FACING, context.getHorizontalDirection().getOpposite())
                .setValue(KilnBlock.LIT, false)
                .setValue(KilnBlock.HALF, Half.BOTTOM);
        CollisionContext collisioncontext = context.getPlayer() == null ? CollisionContext.empty() : CollisionContext.of(context.getPlayer());
        if (! context.getLevel().isUnobstructed(toPlace, target, collisioncontext))
        {
            if (context.getPlayer() != null)
            {
                context.getPlayer().sendOverlayMessage(Component.translatable("message.vegan_mod.no_room_for_kiln_2"));
            }
            return InteractionResult.FAIL;
        }
        if (! context.getLevel().setBlock(target, toPlace, 11))
        {
            return InteractionResult.FAIL;
        }
        // let's do this:
        context.getLevel().setBlock(target.above(), toPlace.setValue(KilnBlock.HALF, Half.TOP), 11);
        SoundType soundtype = toPlace.getSoundType(context.getLevel(), target, null);
        context.getLevel().playSound(null, target, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        if (context.getPlayer() != null)
        {
            context.getLevel().gameEvent(GameEvent.BLOCK_PLACE, target, GameEvent.Context.of(context.getPlayer(), toPlace));
        }
        if (context.getPlayer() == null || ! context.getPlayer().hasInfiniteMaterials())
        {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
