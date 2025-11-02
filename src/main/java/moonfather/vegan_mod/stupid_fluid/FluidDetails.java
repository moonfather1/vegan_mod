package moonfather.vegan_mod.stupid_fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Supplier;

import javax.annotation.Nullable;

public class FluidDetails
{
    public final Supplier<FluidType> type;
    public final FluidType.Properties typeProperties;
    public final Supplier<LiquidBlock> block;
    public final Supplier<BucketItem> bucket;
    private BaseFlowingFluid.Properties properties;
    public final Supplier<BaseFlowingFluid.Source> source;
    public final Supplier<BaseFlowingFluid.Flowing> flowing;

    public FluidDetails(String name, FluidType.Properties typeProperties,
                        @Nullable FluidProperties additionalProperties,
                        BlockBehaviour.Properties blockProperties, Item.Properties itemProperties) {
        this.typeProperties = typeProperties;
        this.type = () -> new FluidType(this.typeProperties) ;
        this.source = () -> new BaseFlowingFluid.Source(this.properties);
        this.flowing = () -> new BaseFlowingFluid.Flowing(this.properties);

        this.properties = new BaseFlowingFluid.Properties(this.type, this.source, this.flowing);
        if (additionalProperties != null) {
            this.properties.explosionResistance(additionalProperties.explosionResistance())
                    .levelDecreasePerBlock(additionalProperties.levelDecreasePerBlock())
                    .slopeFindDistance(additionalProperties.slopeFindDistance()).tickRate(additionalProperties.tickRate());
        }

        this.block = () -> new LiquidBlock(this.source.get(), blockProperties);
        this.properties.block(this.block);

        this.bucket = () -> new BucketItem(this.source.get(), itemProperties);
        this.properties.bucket(this.bucket);

        FluidRegistration.registerFluidType(name, this.type);
        FluidRegistration.registerFluid(name + "_source", this.source);
        FluidRegistration.registerFluid(name + "_flowing", this.flowing);
        FluidRegistration.registerBlock(name, this.block);
        FluidRegistration.registerItem(name + "_bucket", this.bucket);
    }

    public FluidDetails(String name, FluidType.Properties typeProperties,
                                  BlockBehaviour.Properties blockProperties,
                                  Item.Properties itemProperties) {
        this(name, typeProperties, null, blockProperties, itemProperties);
    }

    public BaseFlowingFluid.Properties getProperties() {
        return this.properties;
    }
}
