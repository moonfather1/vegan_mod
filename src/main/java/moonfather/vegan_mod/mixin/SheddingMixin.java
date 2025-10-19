package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.OptionsCommon;
import moonfather.vegan_mod.changes.SheddingHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class SheddingMixin
{
	@Inject(at = @At("TAIL"), method = "tickNonPassenger")
	private void checkForShedding(Entity entity, CallbackInfo info)
	{
		if (entity.tickCount % SheddingHandler.SHEDDING_CHECK_INTERVAL != SheddingHandler.SHEDDING_CHECK_INTERVAL - 3) { return; } // 2 seconds
		if (OptionsCommon.doesEntityShed(entity))
		{
			if (entity.getRandom().nextInt(10) == 7)
			{
				SheddingHandler.maybeShed(entity);
			}
		}
	}
}