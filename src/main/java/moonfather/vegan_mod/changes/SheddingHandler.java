package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.OptionsCommon;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;

public class SheddingHandler
{
    public static InteractionResult onRightClickEntity(Player player, Level level, InteractionHand interactionHand, Entity entity, EntityHitResult entityHitResult)
    {
        if (player != null && player.getItemInHand(interactionHand).is(ConventionalItemTags.BRUSH_TOOLS))
        {
            if (OptionsCommon.doesEntityShed(entity) || entity instanceof Armadillo)
            {
                player.displayClientMessage(MESSAGE, true);
                return InteractionResult.SUCCESS_NO_ITEM_USED;
            }
        }
        return InteractionResult.PASS;
    }
    private static final Component MESSAGE = Component.translatable("message.vegan_mod.do_not_brush").withColor(0xada489);

    ////////////////////

    public static void maybeShed(Entity entity)
    {
        if (entity.level().isClientSide) { return; }
        if (entity instanceof LivingEntity le && le.isBaby()) { return; }
        if (! entity.isAlive() || entity.isRemoved() || entity.isSpectator())  { return; }
        int randomTarget = OptionsCommon.getEntityShedIntervalInSeconds(entity) * 20 / SHEDDING_CHECK_INTERVAL;  // 900s, 2s  ->  1/450 odds
        if (entity.getRandom().nextInt(randomTarget) != 6) { return; }
        entity.playSound(SoundEvents.ARMADILLO_BRUSH, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
        entity.spawnAtLocation(OptionsCommon.getEntityShedItem(entity));
        entity.gameEvent(GameEvent.ENTITY_PLACE);
    }
    public static final int SHEDDING_CHECK_INTERVAL = 2*20; // 2 sec
}
