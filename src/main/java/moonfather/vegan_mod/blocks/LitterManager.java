package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LitterManager
{
    public static void onEntityTick(ItemEntity itemEntity)
    {
        if (! itemEntity.level().isClientSide())
        {
            if (itemEntity.tickCount % 20 == 13 && itemEntity.tickCount > 10 * 20)  // every second
            {
                if (itemEntity.getDeltaMovement().x() > 1e-5 || itemEntity.getDeltaMovement().z() > 1e-5)
                {
                    return; //todo delta movement on contraptions is zero
                }
                if (itemEntity.getItem().is(Items.FEATHER))
                {
                    boolean placed = false;
                    if (! placed) { placed = tryPlace(itemEntity.level(), itemEntity.getOnPos()); }
                    Direction direction = Direction.fromYRot(itemEntity.level().getRandom().nextInt(360));
                    if (! placed) { placed = tryPlace(itemEntity.level(), itemEntity.getOnPos().relative(direction)); }
                    if (! placed) { placed = tryPlace(itemEntity.level(), itemEntity.getOnPos().relative(direction.getClockWise())); }
                    if (! placed) { placed = tryPlace(itemEntity.level(), itemEntity.getOnPos().relative(direction.getOpposite())); }
                    if (! placed) { placed = tryPlace(itemEntity.level(), itemEntity.getOnPos().relative(direction.getCounterClockWise())); }
                    if (placed)
                    {
                        if (itemEntity.getItem().getCount() == 1)
                        {
                            itemEntity.remove(Entity.RemovalReason.DISCARDED);
                        }
                        else
                        {
                            itemEntity.getItem().shrink(1);
                        }
                    }
                }
            }
        }
    }

    private static boolean tryPlace(Level level, BlockPos below)
    {
        BlockState state = level.getBlockState(below.above());
        if (state.is(VeganMod.Blocks.LITTER_OF_FEATHERS))
        {
            int count = state.getValue(LitterBlock.AMOUNT);
            if (count < 4)
            {
                level.setBlockAndUpdate(below.above(), state.setValue(LitterBlock.AMOUNT, count + 1));
                return true;
            }
            if (level.getRandom().nextInt(4) == 2) { return true; } // 25% to discard
            return false;
        }
        if (random == null)
        {
            random = level.getRandom().fork();
        }
        BlockState stateBelow = level.getBlockState(below);
        if (state.isAir() && stateBelow.isFaceSturdy(level, below, Direction.UP))
        {
            Direction facing= Direction.fromYRot(random.nextInt(360));
            level.setBlockAndUpdate(below.above(), VeganMod.Blocks.LITTER_OF_FEATHERS.defaultBlockState().setValue(LitterBlock.AMOUNT, 1).setValue(LitterBlock.FACING, facing));
            return true;
        }
        BlockPos below2 = below.below();
        if (state.isAir() && stateBelow.isAir() && level.getBlockState(below2).isFaceSturdy(level, below2, Direction.UP))
        {
            Direction facing= Direction.fromYRot(random.nextInt(360));
            level.setBlockAndUpdate(below, VeganMod.Blocks.LITTER_OF_FEATHERS.defaultBlockState().setValue(LitterBlock.AMOUNT, 1).setValue(LitterBlock.FACING, facing));
            return true;
        }
        return false;
    }
    private static RandomSource random = null;
}
