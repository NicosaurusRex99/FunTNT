package nicusha.tnt.entities.tnt;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nicusha.tnt.Config;
import nicusha.tnt.registry.ModEntities;

import javax.annotation.Nullable;
import java.util.List;

public class BabyBoomerEntity extends PrimedTnt {
    public BabyBoomerEntity(EntityType<? extends BabyBoomerEntity> type, Level level) {
        super(type, level);
    }

    public BabyBoomerEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.BABY_BOOMER.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(Config.BABY_BOOMER_FUSE.get());
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(owner);
    }

    @Override
    protected void explode() {
        if (this.level() instanceof ServerLevel serverLevel) {
            int radius = Config.BABY_BOOMER_RADIUS.getAsInt();
            AABB area = this.getBoundingBox().inflate(radius);
            List<AgeableMob> nearbyMobs = serverLevel.getEntitiesOfClass(AgeableMob.class, area);
            for (AgeableMob mob : nearbyMobs) {
                if (this.distanceToSqr(mob) <= radius * radius) {
                    mob.setAge(-24000);
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, mob.getX(), mob.getY() + 0.5, mob.getZ(), 7, 0.2, 0.2, 0.2, 0.05);
                }
            }
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.2F);
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);

            this.discard();
        }
    }
}