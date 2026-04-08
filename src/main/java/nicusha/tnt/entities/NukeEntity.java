package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nicusha.tnt.FunTNT;
import nicusha.tnt.Utils;
import nicusha.tnt.registry.ModEntities;
import org.jetbrains.annotations.Nullable;

public class NukeEntity extends PrimedTnt {

    private static final int DEFAULT_FUSE = 300;

    public NukeEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public NukeEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(ModEntities.NUKE.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(DEFAULT_FUSE);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(igniter);
    }

    @Override
    protected void explode() {
        int radius = 128;
        BlockPos center = this.blockPosition();

        if (!this.level().isClientSide()) {
            String quote = FunTNT.MODID + ".too_late";
            var players = this.level().getEntitiesOfClass(net.minecraft.world.entity.player.Player.class, this.getBoundingBox().inflate(radius));

            for (var player : players) {
                player.sendSystemMessage(Component.translatable(quote).withStyle(net.minecraft.ChatFormatting.DARK_PURPLE, net.minecraft.ChatFormatting.BOLD));
            }

            DamageSource customNukeDamage = Utils.nukeDamageSource(this.level(), this);
            var allEntities = this.level().getEntities(this, this.getBoundingBox().inflate(radius));

            for (var entity : allEntities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(customNukeDamage, 10000.0F);
                }
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 100, 5.0, 5.0, 5.0, 0.1);
                serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 200, 10.0, 2.0, 10.0, 0.05);
            }

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + y * y + z * z <= radius * radius) {
                            BlockPos targetPos = center.offset(x, y, z);
                            BlockState state = this.level().getBlockState(targetPos);

                            if (!state.isAir() && state.getDestroySpeed(this.level(), targetPos) >= 0) {
                                this.level().removeBlock(targetPos, false);
                            }
                            if (!state.getFluidState().isEmpty()) {
                                this.level().setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                            else if (!state.isAir() && state.getDestroySpeed(this.level(), targetPos) >= 0) {
                                this.level().removeBlock(targetPos, false);
                            }
                        }
                    }
                }
            }
        }
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 10.0F, Level.ExplosionInteraction.NONE);

    }
}