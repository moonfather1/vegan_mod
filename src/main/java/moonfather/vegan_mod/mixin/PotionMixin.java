package moonfather.vegan_mod.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.class)
public class PotionMixin
{
	@Inject(at = @At("TAIL"), method = "addVanillaMixes")
	private static void appendOurPotions(PotionBrewing.Builder builder,  CallbackInfo info)
	{
		builder.addMix(Potions.MUNDANE, Items.BLUE_ORCHID, Potions.LEAPING);
		Item waterBreathingPotionInput = BuiltInRegistries.ITEM.get(ResourceLocation.parse("playablepeaceful_items:sponge_piece"));
		if (waterBreathingPotionInput.equals(Items.AIR))
		{
			waterBreathingPotionInput = Items.SEAGRASS;
		}
		builder.addMix(Potions.AWKWARD, waterBreathingPotionInput, Potions.WATER_BREATHING);
	}
}