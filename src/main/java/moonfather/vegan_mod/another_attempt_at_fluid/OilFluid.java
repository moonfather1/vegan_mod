package moonfather.vegan_mod.another_attempt_at_fluid;

import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public abstract class OilFluid
{
    public static BaseFlowingFluid.Properties PROPERTIES = new BaseFlowingFluid.Properties(
            FluidRegistries.OIL_TYPE,
            FluidRegistries.OIL_SOURCE,
            FluidRegistries.OIL_FLOWING
    )
            .bucket(FluidRegistries.OIL_BUCKET)
            .explosionResistance(120.0f)
            .block(FluidRegistries.OIL_BLOCK)
            .tickRate(10)  // Half the speed of water
            .slopeFindDistance(4)
            .levelDecreasePerBlock(2);

    public static Flowing FLOWING = new Flowing(PROPERTIES);
    public static Source SOURCE = new Source(PROPERTIES);

    /////////////////////////////////////////////////////////

    public static class Flowing extends BaseFlowingFluid.Flowing
    {
        protected Flowing(Properties properties)
        {
            super(properties);
        }
    }

    public static class Source extends BaseFlowingFluid.Source
    {
        public Source(Properties properties)
        {
            super(properties);
        }
    }
}
