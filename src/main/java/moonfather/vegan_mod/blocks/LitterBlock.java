package moonfather.vegan_mod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.time.LocalTime;
import java.util.List;

public class LitterBlock extends Block
{
    private static final int MIN_PIECES = 1;
    private static final int MAX_PIECES = 4;
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final IntegerProperty AMOUNT = IntegerProperty.create("amount", MIN_PIECES, MAX_PIECES);
    private static final VoxelShape SIMPLE_SHAPE = Block.box(1,0,1,15,1,15);
    private final Item madeOutOf;

    public LitterBlock(Item madeOutOf)
    {
        super(Properties.of().strength(0.8f, 0.0f).sound(SoundType.AZALEA_LEAVES).pushReaction(PushReaction.DESTROY).ignitedByLava().noCollission().noOcclusion().replaceable().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, Integer.valueOf(1)));
        this.madeOutOf = madeOutOf;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_272634_) {
        p_272634_.add(FACING, AMOUNT);
    }

    ////////////////////////////////////////////////////////////

    @Override
    public VoxelShape getShape(BlockState p_273399_, BlockGetter p_273568_, BlockPos p_273314_, CollisionContext p_273274_) { return SIMPLE_SHAPE; }
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.empty(); }

    ////////////////////////////////////////////////////////////

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowBlockState = level.getBlockState(below);
        return belowBlockState.isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean movedByPiston)
    {
        super.neighborChanged(state, level, pos, block, pos2, movedByPiston);
        if (pos2.getY() == pos.getY() - 1)
        {
            if (! level.isClientSide && ! this.canSurvive(state,  level, pos))
            {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) { return true; }
    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) { return true; }
    @Override
    public boolean canBeReplaced(BlockState p_272922_, BlockPlaceContext p_273534_) { return  true; }

    ///////////////////////////////////////////////////////////////


    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
    {
        return this.madeOutOf.getDefaultInstance();
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params)
    {
        ItemStack toDrop = this.madeOutOf.getDefaultInstance();
        toDrop.setCount(state.getValue(AMOUNT));
        return List.of(toDrop);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        System.out.printf("~~ |%d:%d:%d|  use at %d,%d %n", LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), pos.getX(), pos.getZ());
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
    {
        System.out.printf("~~ |%d:%d:%d|  ranfom tick at %d,%d %n", LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), pos.getX(), pos.getZ());
        super.randomTick(state, level, pos, random);
    }
}
