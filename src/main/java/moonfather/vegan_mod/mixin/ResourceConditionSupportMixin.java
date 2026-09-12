package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.TagConditionSupport;
import net.minecraft.core.Registry;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ResourceConditionSupportMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	private void afterTick(CallbackInfo info)
	{
        TagConditionSupport.INSTANCE.setTagCollection(postponedTags);
	}

    @Final
    @Shadow
    private List<Registry.PendingTags<?>> postponedTags;
}