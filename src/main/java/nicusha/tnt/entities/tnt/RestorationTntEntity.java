package nicusha.tnt.entities.tnt;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import nicusha.tnt.Config;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.utils.TerrainRestorer;

import javax.annotation.Nullable;

public class RestorationTntEntity extends PrimedTnt {
    public RestorationTntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public RestorationTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.RESTORATION.get(), level);
        this.setPos(x, y, z);
        this.setFuse(Config.RESTORATION_FUSE.get());
        this.owner = EntityReference.of(owner);
    }

    @Override
    protected void explode() {
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            int radius = Config.RESTORATION_RADIUS.get();
            BlockPos tntPos = this.blockPosition();
            this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), (float)radius, Level.ExplosionInteraction.NONE);
            TerrainRestorer.restoreArea(serverLevel, tntPos, radius);
        }
    }
}