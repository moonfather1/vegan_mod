package moonfather.vegan_mod.another_attempt_at_fluid;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class OilFluidType extends FluidType
{
    public OilFluidType()
    {
        super(Properties.create()
                // Taken from net.neoforged.neoforge.common.NeoForgeMod.WATER_TYPE
                .fallDistanceModifier(0.3F)
                .canExtinguish(true)
                .canConvertToSource(false)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(false)
                .density(300)
                .viscosity(2000)
                .canDrown(true)
        );
    }
}