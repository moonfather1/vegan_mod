package moonfather.vegan_mod.blocks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;

import java.util.Optional;

public abstract class StandardContainerBlockEntity extends BlockEntity
{
    public StandardContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }

    ///////////////////////////////////

    protected abstract Container getContainer();
    //private final List<ItemStack> items = new ArrayList<ItemStack>(9);
    private int capacity = 9;

    public int getCapacity()
    {
        return capacity;
    }

    public void setCapacity(int value)
    {
        this.capacity = value;
    }
    protected void VerifyCapacity()
    {
        //for (int i = this.items.size(); i < this.capacity; i++) { this.items.add(ItemStack.EMPTY); }
    }

    ///////////////////////////////////

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);
        this.VerifyCapacity();
        for (int i = 0; i < this.capacity; i++)
        {
            Optional<ItemStack> readValue = input.read("item" + i, ItemStack.CODEC);
            if (readValue.isPresent())
            {
                this.getContainer().setItem(i, readValue.get());
            }
            else
            {
                this.getContainer().setItem(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);
        this.saveInternal(output);
    }

    protected void saveInternal(ValueOutput output)
    {
        this.VerifyCapacity();
        for (int i = 0; i < this.capacity; i++)
        {
            //if (! this.items.get(i).isEmpty())
            if (! this.getContainer().getItem(i).isEmpty())
            {
                output.store("item" + i, ItemStack.CODEC, this.getContainer().getItem(i));
            }
            else
            {
                output.discard("item" + i);
            }
        }
    }

    ////////////////////////////////////////////////////////////



    @Override
    public void handleUpdateTag(ValueInput input)
    {
        this.loadWithComponents(input);  // update client
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider)
    {
        try (ProblemReporter.ScopedCollector pr = new ProblemReporter.ScopedCollector(LOGGER))
        {
            TagValueOutput output = TagValueOutput.createWithContext(pr.forChild(this.problemPath()), lookupProvider);
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

    ////////////////////////////////////////////////////////////

    protected void dropEverything()
    {
        this.VerifyCapacity();
        for (int i = 0; i < this.capacity; i++)
        {
            if (! this.getContainer().getItem(i).isEmpty())
            {
                //Block.popResource(this.level, this.getBlockPos(), this.items.get(i));
                Block.popResource(this.level, this.getBlockPos(), this.getContainer().getItem(i));
                this.clearItem(i);
            }
        }
    }

    protected ItemStack getStoredItem(int slot)
    {
        this.VerifyCapacity();
        return this.getContainer().getItem(slot);
    }

    protected void storeItem(int slot, ItemStack itemStack)
    {
        this.VerifyCapacity();
        //this.items.set(slot, itemStack);
        this.getContainer().setItem(slot, itemStack);
        this.setChanged();
    }

    protected void clearItem(int slot)
    {
        this.VerifyCapacity();
        //this.items.set(slot, ItemStack.EMPTY);
        this.getContainer().setItem(slot, ItemStack.EMPTY);
        this.setChanged();
    }
}
