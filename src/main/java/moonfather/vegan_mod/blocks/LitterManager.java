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
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class LitterManager
{
    public static void onEntityTick(EntityTickEvent.Post event)
    {
        if (! event.getEntity().level().isClientSide())
        {
            if (event.getEntity().tickCount % 20 == 13 && event.getEntity().tickCount > 10 * 20)  // every second
            {
                if (event.getEntity().getDeltaMovement().x() > 1e-5 || event.getEntity().getDeltaMovement().z() > 1e-5)
                {
                    return; //todo delta movement on contraptions is zero
                }
                if (event.getEntity() instanceof ItemEntity ie && ie.getItem().is(Items.FEATHER))
                {
                    boolean placed = false;
                    if (! placed) { placed = tryPlace(ie.level(), ie.getOnPos()); }
                    Direction direction = Direction.fromYRot(ie.level().getRandom().nextInt(360));
                    if (! placed) { placed = tryPlace(ie.level(), ie.getOnPos().relative(direction)); }
                    if (! placed) { placed = tryPlace(ie.level(), ie.getOnPos().relative(direction.getClockWise())); }
                    if (! placed) { placed = tryPlace(ie.level(), ie.getOnPos().relative(direction.getOpposite())); }
                    if (! placed) { placed = tryPlace(ie.level(), ie.getOnPos().relative(direction.getCounterClockWise())); }
                    if (placed)
                    {
                        if (ie.getItem().getCount() == 1)
                        {
                            ie.remove(Entity.RemovalReason.DISCARDED);
                        }
                        else
                        {
                            ie.getItem().shrink(1);
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
            level.setBlockAndUpdate(below.above(), VeganMod.Blocks.LITTER_OF_FEATHERS.get().defaultBlockState().setValue(LitterBlock.AMOUNT, 1).setValue(LitterBlock.FACING, facing));
            return true;
        }
        BlockPos below2 = below.below();
        if (state.isAir() && stateBelow.isAir() && level.getBlockState(below2).isFaceSturdy(level, below2, Direction.UP))
        {
            Direction facing= Direction.fromYRot(random.nextInt(360));
            level.setBlockAndUpdate(below, VeganMod.Blocks.LITTER_OF_FEATHERS.get().defaultBlockState().setValue(LitterBlock.AMOUNT, 1).setValue(LitterBlock.FACING, facing));
            return true;
        }
        return false;
    }
    private static RandomSource random = null;
}
