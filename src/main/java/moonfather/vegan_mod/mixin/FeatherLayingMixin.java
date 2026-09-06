package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.blocks.LitterManager;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class FeatherLayingMixin
{
	@Inject(at = @At("TAIL"), method = "tick")
	private void afterTick(CallbackInfo info)
	{
		if (Config.litterEnabled())
		{
            LitterManager.onEntityTick((ItemEntity) (Object) this);
        }
	}
}