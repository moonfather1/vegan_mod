package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class SheddingHandler
{
    public static void onEntityTick(EntityTickEvent.Pre event)
    {
        if (event.getEntity().tickCount % SheddingHandler.SHEDDING_CHECK_INTERVAL != SheddingHandler.SHEDDING_CHECK_INTERVAL - 3) { return; } // 2 seconds
        if (Config.doesEntityShed(event.getEntity()))
        {
            SheddingHandler.maybeShed(event.getEntity());
        }
    }

    public static void onEntityRightClick(PlayerInteractEvent.EntityInteractSpecific event)
    {
        if (event.getItemStack().is(Tags.Items.TOOLS_BRUSH))
        {
            if (Config.doesEntityShed(event.getTarget()) || event.getTarget() instanceof Armadillo)
            {
                event.getEntity().sendOverlayMessage(MESSAGE);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
    private static final Component MESSAGE = Component.translatable("message.vegan_mod.do_not_brush").withColor(0xada489);

    /////////////////////////////////////////////

    public static void maybeShed(Entity entity)
    {
        if (entity.level() instanceof ServerLevel sl)
        {
            if (entity instanceof LivingEntity le && le.isBaby()) { return; }
            if (! entity.isAlive() || entity.isSpectator() || entity.isRemoved()) { return; }
            int randomTarget = Config.getEntityShedIntervalInSeconds(entity) * 20 / SHEDDING_CHECK_INTERVAL;  // 900s, 2s  ->  1/450 odds
            if (entity.getRandom().nextInt(randomTarget) != 6) { return; }
            entity.playSound(SoundEvents.ARMADILLO_BRUSH, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
            entity.spawnAtLocation(sl, Config.getEntityShedItem(entity));
            entity.gameEvent(GameEvent.ENTITY_PLACE);
        }
    }
    public static final int SHEDDING_CHECK_INTERVAL = 2*20; //
}
