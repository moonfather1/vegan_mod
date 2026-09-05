package moonfather.vegan_mod.mixin;

import moonfather.vegan_mod.changes.DontAskAndDontLookHere;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ocelot.class)
public abstract class CatGoalMixin extends Animal
{
    private CatGoalMixin(EntityType<? extends Animal> entityType, Level level) { super(entityType, level); }
    @Unique
    private DontAskAndDontLookHere.OcelotTemptGoal2 temptGoal2 = null;

    @Inject(at = @At("HEAD"), method = "registerGoals")
    private void addGoal(CallbackInfo info)
    {
        this.temptGoal2 = new DontAskAndDontLookHere.OcelotTemptGoal2((Ocelot) (Object) this, p_335596_ -> p_335596_.is(Items.MILK_BUCKET));
        this.goalSelector.addGoal(3, this.temptGoal2);
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void activate(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir)
    {
        ItemStack itemstack = player.getItemInHand(hand);
        CatGoalAccessor accessor = ((CatGoalAccessor) (Object) this);
        if ((this.temptGoal2 == null || this.temptGoal2.isRunning()) && ! accessor.invokeIsTrusting() && itemstack.is(Items.MILK_BUCKET) && player.distanceToSqr(this) < 7.0)
        {
            if (! this.level().isClientSide())
            {
                player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                if (this.random.nextInt(6) > 0)
                {
                    accessor.invokeSetTrusting(true);
                    accessor.invokeSpawnTrustingParticles(true);
                    this.level().broadcastEntityEvent(this, (byte)41);
                }
                else
                {
                    accessor.invokeSpawnTrustingParticles(false);
                    this.level().broadcastEntityEvent(this, (byte)40);
                }
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
        }
    }
}
