package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import nicusha.tnt.registry.ModEntities;

import javax.annotation.Nullable;
import java.util.List;

public class CryoTntEntity extends PrimedTnt {

    public CryoTntEntity(EntityType<? extends CryoTntEntity> type, Level level) {
        super(type, level);
    }
    public CryoTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.CRYO.get(), level);
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
    protected void explode() {
        float radius = 6.0F;
        BlockPos center = this.blockPosition();

        if (!this.level().isClientSide()) {
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-6, -3, -6), center.offset(6, 3, 6))) {
                if (pos.closerThan(center, radius)) {
                    BlockState state = this.level().getBlockState(pos);
                    if (state.is(Blocks.WATER)) {
                        this.level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
                    }
                    else if (state.is(Blocks.LAVA)) {
                        this.level().setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                    }
                    else if (state.is(Blocks.FIRE)) {
                        this.level().removeBlock(pos, false);
                    }
                    else if (state.isAir() && this.level().getBlockState(pos.below()).isSolid()) {
                        if (this.random.nextFloat() < 0.6f) {
                            this.level().setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                        }
                    }
                }
            }

            AABB area = new AABB(center).inflate(radius);
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                entity.setTicksFrozen(entity.getTicksFrozen() + 400);
                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 400, 3, true, false));
            }

            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
            this.level().levelEvent(2004, center, 0);
        }
        this.discard();
    }
}