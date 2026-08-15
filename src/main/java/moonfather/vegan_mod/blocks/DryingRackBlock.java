package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DryingRackBlock extends Block implements EntityBlock
{
    public DryingRackBlock(String id)
    {
        ResourceKey<Block> bigId = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(VeganMod.MODID, id));
        super(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_BROWN).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.BAMBOO_WOOD_HANGING_SIGN).strength(0.8f, 0.5f).setId(bigId));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return TABLE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) { return TABLE; }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return COLI;
    }

    private static final VoxelShape TOP = Block.box(1,8,2,15,11,14);
    private static final VoxelShape COLI = Block.box(1,0,2,15,11,14);
    private static final VoxelShape LEG1 = Block.box(0,0,0,3,11,2);
    private static final VoxelShape LEG2 = Block.box(13,0,0,16,11,2);
    private static final VoxelShape LEG3 = Block.box(0,0,14,3,11,16);
    private static final VoxelShape LEG4 = Block.box(13,0,14,16,11,16);
    private static final VoxelShape TABLE = Shapes.or(TOP, LEG1, LEG2, LEG3, LEG4);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 40; }
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 20; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        return VeganMod.BlockEntities.DRYING_RACK_BE.get().create(pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_)
    {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////

    private final MutableComponent RackMessage = Component.translatable("message.vegan_mod.invalid_item_for_rack");

    protected boolean canDepositItem(ItemStack mainHandItem)
    {
        return DataMapManager.getRecipe(mainHandItem) != null;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult)
    {
        if (level.isClientSide())
        {
            return InteractionResult.SUCCESS;
        }
        DryingRackBlockEntity BE = ((DryingRackBlockEntity) level.getBlockEntity(pos));
        ItemStack existing = BE.getItem();
        ItemStack itemInMainHand = player.getMainHandItem();
        ItemStack itemInOffHand = player.getOffhandItem();
        if (existing.isEmpty() && ! itemInMainHand.isEmpty())
        {
            if (! this.canDepositItem(itemInMainHand))
            {
                player.sendSystemMessage(RackMessage);
                return InteractionResult.CONSUME;
            }
            //System.out.println("~~~~~ADDED FROM MAIN");
            ItemStack toStore = itemInMainHand.copy();
            toStore.setCount(1);
            BE.depositItem(toStore);
            itemInMainHand.shrink(1);
            player.playSound(SoundEvents.WOOD_PLACE, 0.5f, 0.7f);
        }
        else if (existing.isEmpty() && itemInMainHand.isEmpty() && itemInOffHand.isEmpty())
        {
            //System.out.println("~~~~~EMPTY TO EMPTY");
        }
        else if (existing.isEmpty() && itemInMainHand.isEmpty() && ! itemInOffHand.isEmpty())
        {
            if (! this.canDepositItem(itemInOffHand))
            {
                player.sendSystemMessage(RackMessage);
                return InteractionResult.CONSUME;
            }
            //System.out.println("~~~~~ADDED FROM OFFHAND");
            ItemStack toStore = itemInOffHand.copy();
            toStore.setCount(1);
            BE.depositItem(toStore);
            itemInOffHand.shrink(1);
            player.playSound(SoundEvents.WOOD_PLACE, 0.5f, 0.7f);
        }
        else if (! existing.isEmpty() && itemInMainHand.isEmpty())
        {
            //System.out.println("~~~~~TAKEN WITH MAIN");
            player.setItemInHand(InteractionHand.MAIN_HAND, existing);
            BE.clearItem();
            player.playSound(SoundEvents.ITEM_PICKUP, 0.5f, 1);
        }
        else if (! existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, itemInMainHand) && ! canDepositItem(itemInMainHand))
        {
            //System.out.println("~~~~~BOTH FULL, WILL TAKE");
            itemInMainHand.grow(1);
            BE.clearItem();
            player.playSound(SoundEvents.ITEM_PICKUP, 0.5f, 1);
        }
        else
        {
            //System.out.println("~~~~~BOTH FULL");
            return InteractionResult.CONSUME; // no need to send update message
        }
        level.sendBlockUpdated(pos, blockState, blockState, 2);
        return InteractionResult.CONSUME;
    }

    /////////////////////////////////////////////////////


    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity rack)
        {
            rack.onRandomBlockTick();
        }
    }
}
