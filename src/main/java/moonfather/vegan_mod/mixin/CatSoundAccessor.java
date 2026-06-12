package moonfather.vegan_mod.mixin;

import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Cat.class)
public interface CatSoundAccessor
{
    @Invoker("getSoundSet")
    CatSoundVariant.CatSoundSet invokeGetSoundSet();
}
