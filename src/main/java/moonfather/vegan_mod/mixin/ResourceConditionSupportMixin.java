package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.TagConditionSupport;
import moonfather.vegan_mod.blocks.LitterManager;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagManager;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadableServerResources.class)
public class ResourceConditionSupportMixin
{
	@Inject(at = @At("TAIL"), method = "<init>")
	private void afterTick(CallbackInfo info)
	{
        TagConditionSupport.INSTANCE.setTagManager(tagManager);
	}

    @Final
    @Shadow
    private TagManager tagManager;
}