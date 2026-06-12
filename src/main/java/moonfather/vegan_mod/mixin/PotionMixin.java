package moonfather.vegan_mod.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PotionBrewing.class)
public class PotionMixin
{
	@Inject(at = @At("TAIL"), method = "addVanillaMixes")
	private static void appendOurPotions(PotionBrewing.Builder builder,  CallbackInfo info)
	{
		builder.addMix(Potions.MUNDANE, Items.BLUE_ORCHID, Potions.LEAPING);
		Item waterBreathingPotionInput = Items.SEAGRASS;
        Optional<Holder.Reference<Item>> ohr = BuiltInRegistries.ITEM.get(Identifier.parse("playablepeaceful_items:sponge_piece"));
		if (ohr.isPresent())
		{
			waterBreathingPotionInput = ohr.get().value();
		}
		builder.addMix(Potions.THICK, waterBreathingPotionInput, Potions.WATER_BREATHING);
	}
}