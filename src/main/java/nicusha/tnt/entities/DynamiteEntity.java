package nicusha.tnt.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;

public class DynamiteEntity extends ThrowableItemProjectile {
    public DynamiteEntity(EntityType<? extends DynamiteEntity> type, Level level) {
        super(type, level);
    }

    public DynamiteEntity(Level level, LivingEntity shooter) {
        super(ModEntities.DYNAMITE.get(), shooter, level, ModItems.DYNAMITE.toStack());
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DYNAMITE.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }
}