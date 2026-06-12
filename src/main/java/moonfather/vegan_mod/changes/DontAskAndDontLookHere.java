package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.mixin.CatGoalAccessor;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class DontAskAndDontLookHere
{
    public static class OcelotTemptGoal2 extends TemptGoal {
        private final Ocelot ocelot;

        public OcelotTemptGoal2(Ocelot ocelot, Predicate<ItemStack> items) {
            super(ocelot, 0.4, items, true);
            this.ocelot = ocelot;
        }

        @Override
        protected boolean canScare() {
            return super.canScare() && !((CatGoalAccessor) this.ocelot).invokeIsTrusting();
        }
    }

    public static class CatTemptGoal2 extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final Cat cat;

        public CatTemptGoal2(Cat cat, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
            super(cat, speedModifier, items, canScare);
            this.cat = cat;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.player;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canScare() {
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.player) ? false : super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.cat.isTame();
        }
    }
}
