package moonfather.vegan_mod.mixin;

import net.minecraft.world.entity.animal.Ocelot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Ocelot.class)
public interface CatGoalAccessor
{
    //@Accessor(value = "temptGoal")
    //TemptGoal getTemptGoal();

    @Invoker("isTrusting")
    boolean invokeIsTrusting();

    @Invoker("spawnTrustingParticles")
    void invokeSpawnTrustingParticles(boolean isTrusted);

    @Invoker("setTrusting")
    void invokeSetTrusting(boolean value);
}
