package nicusha.tnt.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import nicusha.tnt.network.payload.SpinPlayerPayload;

public class PartyDanceEffect extends MobEffect {

    public PartyDanceEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF00FF);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplification) {
        float spinAmount = 15.0F;

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new SpinPlayerPayload(spinAmount));

            float serverNewYRot = (serverPlayer.getYRot() + spinAmount) % 360.0F;
            serverPlayer.setYRot(serverNewYRot);
            serverPlayer.setYHeadRot(serverNewYRot);
            serverPlayer.setYBodyRot(serverNewYRot);

        } else if (entity instanceof Mob mob) {
            float mobNewYRot = (mob.getYRot() + spinAmount) % 360.0F;
            mob.setYRot(mobNewYRot);
            mob.setYHeadRot(mobNewYRot);
            mob.setYBodyRot(mobNewYRot);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}