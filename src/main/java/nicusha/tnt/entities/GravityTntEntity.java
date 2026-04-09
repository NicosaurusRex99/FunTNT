package nicusha.tnt.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import nicusha.tnt.registry.ModEntities;

import javax.annotation.Nullable;
import java.util.List;

public class GravityTntEntity extends PrimedTnt {
    public GravityTntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }
    public GravityTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.GRAVITY.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(owner);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getFuse() > 0) {
            double pullRadius = 8.0D;
            AABB gravityWell = this.getBoundingBox().inflate(pullRadius);

            List<Entity> targets = this.level().getEntitiesOfClass(Entity.class, gravityWell);
            Vec3 tntCenter = this.position().add(0, 0.5, 0);

            for (Entity victim : targets) {
                if (victim == this) continue;
                if (victim.isSpectator()) continue;
                if (victim instanceof Player p && p.getAbilities().instabuild) continue;
                Vec3 victimPos = victim.position();
                double distanceSq = tntCenter.distanceToSqr(victimPos);
                if (distanceSq > 0.5D) {
                    Vec3 pullDir = tntCenter.subtract(victimPos).normalize();
                    if (!this.level().isClientSide()) {
                        double strength = 0.15D;
                        if (victim instanceof ItemEntity) {
                            strength = 0.2D;
                        }
                        Vec3 currentMotion = victim.getDeltaMovement();
                        victim.setDeltaMovement(currentMotion.add(pullDir.x * strength, 0.08D, pullDir.z * strength));
                        victim.hurtMarked = true;
                    }
                    if (this.level().isClientSide()) {
                        if (this.random.nextFloat() < 0.3F) {
                            this.level().addParticle(ParticleTypes.PORTAL, victim.getX(), victim.getY() + 0.5D, victim.getZ(), pullDir.x * 0.2D, 0.1D, pullDir.z * 0.2D);
                        }
                    }
                }
            }
        }
    }
}