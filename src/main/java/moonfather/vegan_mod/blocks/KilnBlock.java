package moonfather.vegan_mod.blocks;

import com.mojang.serialization.MapCodec;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KilnBlock extends BaseEntityBlock
{
    public KilnBlock(String shortName)
    {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(3.5F)
                .lightLevel(KilnBlock::getLight)
                .pushReaction(PushReaction.BLOCK)
                .requiresCorrectToolForDrops()
                .setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(VeganMod.MODID, shortName))));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }
    public KilnBlock(Properties p) { super(p); }

    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;



    private static int getLight(BlockState blockState)
    {
        return blockState.getValue(AbstractFurnaceBlock.LIT) ? 10 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LIT, HALF);
    }

    @Override  @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }
    public static final MapCodec<KilnBlock> CODEC = BlockBehaviour.simpleCodec(KilnBlock::new);

    //---------------------------------//

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (level.isClientSide())
        {
            return InteractionResult.SUCCESS;
        }
        else if (state.getValue(HALF).equals(Half.TOP))
        {
            BlockPos below = pos.below();
            return this.useWithoutItem(level.getBlockState(below), level, below, player, hitResult.withPosition(below));
        }
        else
        {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof KilnBlockEntity kbe)
            {
                player.openMenu(kbe);
                player.awardStat(Stats.INTERACT_WITH_FURNACE);
            }
            return InteractionResult.CONSUME;
        }
    }



    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @org.jspecify.annotations.Nullable Orientation orientation, boolean movedByPiston)
    {
        if (state.is(VeganMod.Blocks.KILN.get()))
        {
            if (state.getValue(KilnBlock.HALF) == Half.TOP && ! level.getBlockState(pos.below()).is(VeganMod.Blocks.KILN.get()))
            {
                level.destroyBlock(pos, false);
                return;
            }
            else if (state.getValue(KilnBlock.HALF) == Half.BOTTOM && ! level.getBlockState(pos.above()).is(VeganMod.Blocks.KILN.get()))
            {
                level.destroyBlock(pos, true);
                return;
            }
        }
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player)
    {
        return VeganMod.Blocks.KILN_ITEM.get().getDefaultInstance();
    }

    //-----------------------------------//

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return state.getValue(HALF) == Half.BOTTOM ? Shapes.block() : SLAB;
    }
    private static final VoxelShape SLAB = Shapes.box(0, 0, 0, 1, 0.5, 1);

    @Override @NotNull
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    //-----------------------------------//


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        if (level.isClientSide()) { return null; }
        return KilnBlockEntity::serverTick;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        if (blockState.getValue(HALF).equals(Half.TOP))
        {
            return null;
        }
        return new KilnBlockEntity(blockPos, blockState);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (state.getValue(HALF).equals(Half.BOTTOM))
        {
            // top doesn't get lit anyway but that might change
            Blocks.FURNACE.animateTick(state, level, pos, random);
        }
    }
}
