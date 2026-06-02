package nicusha.tnt.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class PartyDanceEffect extends MobEffect {

    public PartyDanceEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF00FF);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplification) {
        if (entity instanceof Mob mob) {
            float newYRot = (mob.getYRot() + 30.0F) % 360.0F;
            mob.setYRot(newYRot);
            mob.setYHeadRot(newYRot);
            mob.setYBodyRot(newYRot);
            if (mob.tickCount % 10 == 0) {
                mob.getNavigation().stop();
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}