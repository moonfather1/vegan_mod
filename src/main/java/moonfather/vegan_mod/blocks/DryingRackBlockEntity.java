package moonfather.vegan_mod.blocks;

import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class DryingRackBlockEntity extends BlockEntity
{
    public DryingRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }
    public DryingRackBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(VeganMod.BlockEntities.DRYING_RACK_BE.get(), pos, blockState);
    }

    ////////////////////////////////////

    private ItemStack itemOnRack = ItemStack.EMPTY;

    public void depositItem(ItemStack toStore)
    {
        this.itemOnRack = toStore;
        if (this.hasLevel())
        {
            this.startTime = this.getLevel().getGameTime();
        }
        this.setChanged();
    }

    public ItemStack getItem()
    {
        return this.itemOnRack;
    }

    public void dropAll()
    {
        Block.popResource(this.level, this.getBlockPos(), this.itemOnRack);
        this.clearItem();
    }

    public void clearItem()
    {
        this.itemOnRack = ItemStack.EMPTY;
        this.startTime = 0;
        this.setChanged();
    }

    /////////////////////////////////////////////////

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
        CompoundTag tag = pTag.getCompound("item1");
        if (tag.contains("id"))
        {
            this.itemOnRack = ItemStack.parseOptional(lookupProvider, tag);
        }
        else
        {
            this.itemOnRack = ItemStack.EMPTY;
        }
        this.startTime = pTag.getLong("start1");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        this.saveInternal(pTag, lookupProvider);
    }

    protected CompoundTag saveInternal(CompoundTag compoundTag, HolderLookup.Provider lookupProvider)
    {
        if (! this.itemOnRack.isEmpty())
        {
            compoundTag.put("item1", this.itemOnRack.save(lookupProvider, new CompoundTag()));
        }
        else
        {
            compoundTag.put("item1", new CompoundTag());
        }
        compoundTag.putLong("start1", this.startTime);
        return compoundTag;
    }

    ////////////////////////////////////////////////////////////



    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider)
    {
        this.loadWithComponents(tag, lookupProvider); // update client
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider)
    {
        return this.saveInternal(new CompoundTag(), lookupProvider); //send to client
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }

    ////////////////////////////////////////////////

    public void onRandomBlockTick()
    {
        if (this.itemOnRack.isEmpty())
        {
            //System.out.printf("~~ %d,%d skipping because of no item %n", this.getBlockPos().getX(), this.getBlockPos().getZ());
            return;
        }
        if (! this.hasLevel())
        {
            //System.out.printf("~~ %d,%d (%s) skipping because of no level %n", this.getBlockPos().getX(), this.getBlockPos().getZ(), this.itemOnRack.getHoverName().getString());
            return;
        }
        if (this.isRainingAt(this.getBlockPos()))
        {
            //System.out.printf("~~ %d,%d (%s) skipping because of rain %n", this.getBlockPos().getX(), this.getBlockPos().getZ(), this.itemOnRack.getHoverName().getString());
            this.startTime = this.getLevel().getGameTime() + 600;
            this.setChanged();
            return;
        }
        DataMapManager.DryingRecipe recipe = DataMapManager.getRecipe(this.itemOnRack);
        if (recipe == null)
        {
            //System.out.printf("~~ %d,%d (%s) skipping because of no recipe  %n", this.getBlockPos().getX(), this.getBlockPos().getZ(), this.itemOnRack.getHoverName().getString());
            return;
        }
        if (this.startTime <= 0)
        {
            this.startTime = this.getLevel().getGameTime();
        }
        long endTime = this.startTime + recipe.timeInMinutes() * 60L * 20L;
        if (endTime > this.getLevel().getGameTime())
        {
            //System.out.printf("~~ %d,%d (%s) skipping because of timer; %d more sec %n", this.getBlockPos().getX(), this.getBlockPos().getZ(), this.itemOnRack.getHoverName().getString(), (endTime-this.getLevel().getGameTime())*20);
            return;
        }
        if (this.getLevel().getRandom().nextBoolean())
        {
            //System.out.printf("~~ %d,%d (%s) skipping because of coin toss  %n", this.getBlockPos().getX(), this.getBlockPos().getZ(), this.itemOnRack.getHoverName().getString());
            return;
        }
        this.depositItem(recipe.output().value().getDefaultInstance());
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }
    private long startTime = 0;

    public boolean isRainingAt(BlockPos pos)
    {
        if (! this.getLevel().isRaining()) {
            return false;
        } else if (! this.getLevel().canSeeSky(pos)) {
            return false;
        } else if (this.getLevel().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()+1) {
            return false;
        } else {
            Biome biome = this.getLevel().getBiome(pos).value();
            return biome.getPrecipitationAt(pos) == Biome.Precipitation.RAIN;
        }
    }
}
