package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.changes.DontAskAndDontLookHere;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatGoalMixin2 extends Animal
{
    @Shadow public abstract boolean isFood(ItemStack stack);

    private CatGoalMixin2(EntityType<? extends Animal> entityType, Level level) { super(entityType, level); }
    @Unique
    private DontAskAndDontLookHere.CatTemptGoal2 temptGoal2 = null;

    @Inject(at = @At("HEAD"), method = "registerGoals")
    private void addGoal(CallbackInfo info)
    {
        this.temptGoal2 = new DontAskAndDontLookHere.CatTemptGoal2((Cat) (Object) this, 0.4, p_335596_ -> p_335596_.is(Items.MILK_BUCKET), true);
        this.goalSelector.addGoal(4, this.temptGoal2);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void activate(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.MILK_BUCKET))
        {
            Cat cat = ((Cat) (Object) this);
            if (cat.isTame())
            {
                if (! cat.level().isClientSide())
                {
                    if (cat.getRandom().nextInt(3) > 0)
                    { cat.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F); }
                    else
                    { cat.playSound(SoundEvents.CAT_PURREOW, 1.0F, 1.0F); }
                }
                if (this.random.nextInt(7) == 0) { player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());}
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
            else
            {
                if (! this.level().isClientSide())
                {
                    if (cat.getRandom().nextInt(3) == 0 && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(cat, player)) {
                        cat.tame(player);
                        cat.setOrderedToSit(true);
                        cat.level().broadcastEntityEvent(this, (byte)7);
                        if (cat.getRandom().nextInt(3) > 0)
                        { cat.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F); }
                        else
                        { cat.playSound(SoundEvents.CAT_PURREOW, 1.0F, 1.0F); }
                    } else {
                        cat.level().broadcastEntityEvent(this, (byte)6);
                        cat.playSound(SoundEvents.CAT_HISS, 1.0F, 1.0F);
                    }
                    cat.setPersistenceRequired();
                }
                if (this.random.nextInt(5) == 0) { player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());}
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
            }
        }
    }
}
