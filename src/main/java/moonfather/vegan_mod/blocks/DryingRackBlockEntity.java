package moonfather.vegan_mod.blocks;

import com.mojang.logging.LogUtils;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity
{
    public DryingRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }
    public DryingRackBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(VeganMod.Blocks.DRYING_RACK_BE.get(), pos, blockState);
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

    //////////////////////////////////////////////////

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state)
    {
        super.preRemoveSideEffects(pos, state);
        this.dropAll();
    }

    /////////////////////////////////////////////////

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        Optional<ItemStack> readValue = input.read("item1", ItemStack.CODEC);
        if (readValue.isPresent())
        {
            this.itemOnRack = readValue.get();
        }
        else
        {
            this.itemOnRack = ItemStack.EMPTY;
        }
        this.startTime = input.getLongOr("start1", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        this.saveInternal(output);
    }

    protected void saveInternal(ValueOutput output)
    {
        if (! this.itemOnRack.isEmpty())
        {
            output.store("item1", ItemStack.CODEC, this.itemOnRack);
        }
        else
        {
            output.discard("item1");
        }
        output.putLong("start1", this.startTime);
    }

    ////////////////////////////////////////////////////////////


    @Override
    public void handleUpdateTag(ValueInput input)
    {
        this.loadWithComponents(input);  // update client
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        try (ProblemReporter.ScopedCollector pr = new ProblemReporter.ScopedCollector(LOGGER))
        {
            TagValueOutput output = TagValueOutput.createWithContext(pr.forChild(this.problemPath()), registries);
            this.saveInternal(output);
            return output.buildResult();   //send to client
        }
    }
    protected static final Logger LOGGER = LogUtils.getLogger();

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
            return biome.getPrecipitationAt(pos, this.getLevel().getSeaLevel()) == Biome.Precipitation.RAIN;
        }
    }
}
