package nicusha.tnt.entities.tnt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import nicusha.tnt.Config;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.utils.RiftManager;

import javax.annotation.Nullable;

public class RiftEntity extends PrimedTnt {
    public RiftEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public RiftEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.RIFT.get(), level);
        this.setPos(x, y, z);
        this.setFuse(Config.RIFT_FUSE.get());
        this.owner = EntityReference.of(owner);
    }

    @Override
    protected void explode() {
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
            RiftManager.trigger(serverLevel, this.blockPosition(), Config.RIFT_RADIUS.get(), Config.RIFT_TIMEOUT_TICKS.get());
        }
    }
}