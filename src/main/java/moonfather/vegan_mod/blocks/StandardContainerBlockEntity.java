package moonfather.vegan_mod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.loadAdditional(pTag, lookupProvider);
        this.VerifyCapacity();
        for (int i = 0; i < this.capacity; i++)
        {
            CompoundTag tag = pTag.getCompound("item" + i);
            if (tag.contains("id"))
            {
                this.getContainer().setItem(i, ItemStack.parseOptional(lookupProvider, tag));
                //this.items.set(i, ItemStack.parseOptional(lookupProvider, tag));
            }
            else
            {
                this.getContainer().setItem(i, ItemStack.EMPTY);
                //this.items.set(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider lookupProvider)
    {
        super.saveAdditional(pTag, lookupProvider);
        this.saveInternal(pTag, lookupProvider);
    }

    protected CompoundTag saveInternal(CompoundTag compoundTag, HolderLookup.Provider lookupProvider)
    {
        this.VerifyCapacity();
        for (int i = 0; i < this.capacity; i++)
        {
            //if (! this.items.get(i).isEmpty())
            if (! this.getContainer().getItem(i).isEmpty())
            {
                compoundTag.put("item" + i, this.getContainer().getItem(i).save(lookupProvider, new CompoundTag()));
                //compoundTag.put("item" + i, this.items.get(i).save(lookupProvider, new CompoundTag()));
            }
            else
            {
                compoundTag.put("item" + i, new CompoundTag());
            }
        }
        return compoundTag;
    }

    ////////////////////////////////////////////////////////////



//    @Override
//    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider)
//    {
//        this.loadWithComponents(tag, lookupProvider); // update client
//    }

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
