package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;

public class MagmaBombEntity extends ThrowableItemProjectile {

    public MagmaBombEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {super.defineSynchedData(builder);}

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
            level().addParticle(ParticleTypes.LAVA, getX(), getY(), getZ(), 0, 0, 0);
            level().addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0.05, 0);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!level().isClientSide()) {
            BlockPos impactPos = BlockPos.containing(result.getLocation());
            level().explode(this, getX(), getY(), getZ(), 2.5F, Level.ExplosionInteraction.MOB);
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos pos = impactPos.offset(x, y, z);
                        if (level().isEmptyBlock(pos) && level().getRandom().nextFloat() < 0.6F) {
                            level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
                        } else if (level().getBlockState(pos).isSolid() && level().getRandom().nextFloat() < 0.3F) {
                            level().setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
                        }
                    }
                }
            }

            this.discard();
        }
    }

}