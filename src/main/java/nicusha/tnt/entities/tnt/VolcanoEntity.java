package nicusha.tnt.entities.tnt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import nicusha.tnt.Config;
import nicusha.tnt.entities.MagmaBombEntity;
import nicusha.tnt.registry.ModEntities;

import org.jspecify.annotations.Nullable;

public class VolcanoEntity extends PrimedTnt {

    private int coneHeight = 36;
    private int baseRadius = 32;
    private int ventingTicks = 80;
    private int ticksPerLayer = 3;
    private int eruptionTicks = 240;
    private int totalFuse = 424;

    private int ticksExisted = 0;
    private @Nullable BlockPos originPos = null;

    public void setOriginPos(BlockPos pos) {
        this.originPos = pos.immutable();
    }

    public VolcanoEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        if (!level.isClientSide()) {
            setupRandomizedStats(level);
        }
    }

    public VolcanoEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.VOLCANO_TNT.get(), level);
        this.setPos(x, y, z);
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        this.setOriginPos(BlockPos.containing(x, y, z));
        this.owner = EntityReference.of(igniter);
        if (!level.isClientSide()) {
            setupRandomizedStats(level);
        }
    }

    private void setupRandomizedStats(Level level) {
        int configuredHeight = Config.VOLCANO_HEIGHT.get();
        int configuredBaseRadius = Config.VOLCANO_BASE_RADIUS.get();
        this.ticksPerLayer = Math.max(1, Config.VOLCANO_TICKS_PER_LAYER.get());
        int heightVariance = Math.max(1, configuredHeight / 6);
        this.coneHeight = Math.max(24, configuredHeight - heightVariance + level.getRandom().nextInt(heightVariance * 2 + 1));
        int radiusVariance = Math.max(1, configuredBaseRadius / 8);
        this.baseRadius = Math.max(20, configuredBaseRadius - radiusVariance + level.getRandom().nextInt(radiusVariance * 2 + 1));
        this.ventingTicks = Config.VOLCANO_VENTING_TICKS.get();
        this.eruptionTicks = Config.VOLCANO_ERUPTION_TICKS.get();
        this.totalFuse = this.ventingTicks + (this.coneHeight * this.ticksPerLayer) + this.eruptionTicks + 30;
        this.setFuse(this.totalFuse);
    }

    private int findSolidGroundY(ServerLevel level, int x, int z, int startY) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, startY, z);
        while (cursor.getY() > level.getMinY()) {
            BlockState state = level.getBlockState(cursor);
            if (isSolidTerrain(state)) {
                return cursor.getY();
            }
            cursor.move(0, -1, 0);
        }
        return level.getMinY();
    }

    private boolean isSolidTerrain(BlockState state) {
        if (state.isAir() || state.canBeReplaced()) return false;
        if (!state.getFluidState().isEmpty()) return false;
        if (state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS)) return false;
        if (state.is(Blocks.BASALT) || state.is(Blocks.SMOOTH_BASALT) || state.is(Blocks.BLACKSTONE) || state.is(Blocks.TUFF) || state.is(Blocks.MAGMA_BLOCK)) {
            return false;
        }
        return true;
    }

    private int getVolcanoHeightAt(int x, int z) {
        double dist = Math.sqrt(x * x + z * z);
        if (dist > this.baseRadius) return -1;
        double angle = Math.atan2(z, x);
        double radialNoise = Math.sin(angle * 3.0 + 0.8) * 1.5 + Math.cos(angle * 5.0 - 1.1) * 1.0 + Math.sin(angle * 9.0 + 2.0) * 0.5;
        double effectiveDist = Math.max(0.0, dist + radialNoise);
        double norm = Math.min(1.0, effectiveDist / this.baseRadius);
        double height = this.coneHeight * Math.pow(1.0 - norm, 1.35);
        height += (Math.sin(angle * 6.0 + dist * 0.16) * 1.0 + Math.cos(angle * 9.0 - dist * 0.10) * 0.55) * (1.0 - norm);
        double craterRadius = 0.20;
        if (norm < craterRadius) {
            double craterT = norm / craterRadius;
            height -= 7.0 * Math.pow(1.0 - craterT, 1.25);
            if (norm > 0.10) {
                double rimT = (norm - 0.10) / 0.10;
                height += 2.0 * Math.sin(rimT * Math.PI);
            }
        }
        return Math.max(0, (int) Math.round(height));
    }

    private int getScanStart() {
        if (this.originPos == null) return 100;
        return Math.min(this.level().getMaxY() - 1, this.originPos.getY() + this.coneHeight + 10);
    }

    @Override
    public void tick() {
        if (this.originPos == null) {
            this.originPos = BlockPos.containing(this.position());
        }
        this.setNoGravity(true);
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(this.originPos.getX() + 0.5D, this.originPos.getY(), this.originPos.getZ() + 0.5D);
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            this.ticksExisted++;
            this.setFuse(Math.max(1, this.totalFuse - this.ticksExisted));
            int age = this.ticksExisted;
            int coneBuildingEnd = this.ventingTicks + (this.coneHeight * this.ticksPerLayer);
            int eruptionEnd = coneBuildingEnd + this.eruptionTicks;
            if (age < this.ventingTicks) {
                handleVentingPhase(serverLevel, this.originPos, age);
            } else if (age < coneBuildingEnd) {
                handleConeBuildingPhase(serverLevel, this.originPos, age);
            } else if (age < eruptionEnd) {
                handleEruptionPhase(serverLevel, this.originPos, age, coneBuildingEnd);
            } else {
                handleExtinctionPhase(serverLevel, this.originPos, age, eruptionEnd);
            }
        }
    }

    private void handleVentingPhase(ServerLevel level, BlockPos origin, int age) {
        level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, getX(), getY() + 0.5, getZ(), 6, 0.3, 0.8, 0.3, 0.05);
        if (age % 10 == 0) {
            float pitch = 0.4F + ((float) age / this.ventingTicks) * 0.4F;
            level.playSound(null, origin, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.5F, pitch);
        }

        if (age == this.ventingTicks - 4) {
            int radius = this.baseRadius;
            int scanStart = getScanStart();
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (getVolcanoHeightAt(x, z) >= 0) {
                        int targetX = origin.getX() + x;
                        int targetZ = origin.getZ() + z;
                        int colGroundY = findSolidGroundY(level, targetX, targetZ, scanStart);
                        BlockPos target = new BlockPos(targetX, colGroundY + 1, targetZ);
                        BlockState state = level.getRandom().nextInt(4) == 0 ? Blocks.MAGMA_BLOCK.defaultBlockState() : Blocks.TUFF.defaultBlockState();
                        level.setBlock(target, state, 2);
                    }
                }
            }
        }
    }

    private void handleConeBuildingPhase(ServerLevel level, BlockPos origin, int age) {
        int phaseAge = age - this.ventingTicks;
        if (phaseAge % this.ticksPerLayer != 0) return;
        int currentLayer = phaseAge / this.ticksPerLayer;
        if (currentLayer >= this.coneHeight) return;
        int maxR = this.baseRadius;
        int scanStart = getScanStart();
        for (int x = -maxR; x <= maxR; x++) {
            for (int z = -maxR; z <= maxR; z++) {
                int columnPeakHeight = getVolcanoHeightAt(x, z);
                if (columnPeakHeight < currentLayer) continue;
                int targetX = origin.getX() + x;
                int targetZ = origin.getZ() + z;
                int colGroundY = findSolidGroundY(level, targetX, targetZ, scanStart);
                int targetY = colGroundY + 1 + currentLayer;
                BlockPos target = new BlockPos(targetX, targetY, targetZ);
                double dist = Math.sqrt(x * x + z * z);
                double normDist = dist / (double) this.baseRadius;
                if (normDist < 0.06 && currentLayer > 2) {
                    level.setBlock(target, Blocks.AIR.defaultBlockState(), 2);
                    continue;
                }
                boolean isSurfaceBlock = (currentLayer == columnPeakHeight);
                BlockState stateToPlace;
                if (isSurfaceBlock) {
                    stateToPlace = switch (level.getRandom().nextInt(10)) {
                        case 0, 1, 2, 3 -> Blocks.BASALT.defaultBlockState();
                        case 4, 5, 6 -> Blocks.BLACKSTONE.defaultBlockState();
                        case 7, 8 -> Blocks.SMOOTH_BASALT.defaultBlockState();
                        default -> Blocks.MAGMA_BLOCK.defaultBlockState();
                    };
                } else {
                    stateToPlace = (level.getRandom().nextInt(4) == 0) ? Blocks.TUFF.defaultBlockState() : Blocks.BASALT.defaultBlockState();
                }
                level.setBlock(target, stateToPlace, 2);
            }
        }
        BlockPos soundPos = origin.above(currentLayer);
        float soundPitch = 0.5F + ((float) currentLayer / this.coneHeight) * 0.5F;
        level.playSound(null, soundPos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.5F, soundPitch);
    }

    private void handleEruptionPhase(ServerLevel level, BlockPos origin, int age, int coneBuildingEnd) {
        int scanStart = getScanStart();
        int centerGroundY = findSolidGroundY(level, origin.getX(), origin.getZ(), scanStart);
        int topY = getVolcanoHeightAt(0, 0);
        BlockPos ventTop = new BlockPos(origin.getX(), centerGroundY + 1 + Math.max(1, topY), origin.getZ());
        if (age == coneBuildingEnd) {
            level.setBlock(ventTop, Blocks.LAVA.defaultBlockState(), 3);
            level.playSound(null, ventTop, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 4.0F, 0.4F);
        }
        if (age % 6 == 0) {
            MagmaBombEntity bomb = new MagmaBombEntity(ModEntities.MAGMA_BOMB.get(), level);
            bomb.setPos(ventTop.getX() + 0.5, ventTop.getY() + 1.5, ventTop.getZ() + 0.5);
            double angle = level.getRandom().nextDouble() * Math.PI * 2;
            double speed = 0.3 + level.getRandom().nextDouble() * 0.6;
            double vx = Math.cos(angle) * speed;
            double vz = Math.sin(angle) * speed;
            double vy = 1.2 + level.getRandom().nextDouble() * 0.5;
            bomb.setDeltaMovement(vx, vy, vz);
            level.addFreshEntity(bomb);
            level.playSound(null, ventTop, SoundEvents.GHAST_SHOOT, SoundSource.BLOCKS, 2.0F, 0.4F);
        }
        level.sendParticles(ParticleTypes.LAVA, ventTop.getX() + 0.5, ventTop.getY() + 1.2, ventTop.getZ() + 0.5, 12, 0.8, 0.8, 0.8, 0.15);
        level.sendParticles(ParticleTypes.LARGE_SMOKE, ventTop.getX() + 0.5, ventTop.getY() + 2.0, ventTop.getZ() + 0.5, 8, 0.6, 1.2, 0.6, 0.1);
    }

    private void handleExtinctionPhase(ServerLevel level, BlockPos origin, int age, int eruptionEnd) {
        if (age == eruptionEnd) {
            int scanStart = getScanStart();
            int centerGroundY = findSolidGroundY(level, origin.getX(), origin.getZ(), scanStart);
            int topY = getVolcanoHeightAt(0, 0);
            BlockPos craterCenter = new BlockPos(origin.getX(), centerGroundY + 1 + Math.max(1, topY), origin.getZ());
            int craterRadius = 3;
            for (int x = -craterRadius; x <= craterRadius; x++) {
                for (int z = -craterRadius; z <= craterRadius; z++) {
                    double dist = Math.sqrt(x * x + z * z);
                    if (dist <= craterRadius) {
                        BlockPos target = craterCenter.offset(x, 0, z);
                        if (dist <= 1.2) {
                            level.setBlock(target, Blocks.LAVA.defaultBlockState(), 3);
                        } else {
                            level.setBlock(target, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
                        }
                    }
                }
            }
            triggerFibonacciFinale(level, craterCenter);
            this.discard();
        }
    }

    private void triggerFibonacciFinale(ServerLevel level, BlockPos ventPos) {
        double originX = ventPos.getX() + 0.5;
        double originY = ventPos.getY() + 2.0;
        double originZ = ventPos.getZ() + 0.5;
        level.playSound(null, ventPos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 5.0F, 0.3F);
        level.playSound(null, ventPos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 4.0F, 0.5F);
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, originX, originY, originZ, 3, 1.0, 1.0, 1.0, 0.1);
        int totalBombs = 90;
        double goldenAngle = Math.PI * (3.0 - Math.sqrt(5.0));
        for (int i = 0; i < totalBombs; i++) {
            double yNorm = 1.0 - (i / (double) (totalBombs - 1)) * 1.8;
            double radiusAtY = Math.sqrt(Math.max(0.0, 1.0 - yNorm * yNorm));
            double theta = i * goldenAngle;
            double dirX = radiusAtY * Math.cos(theta);
            double dirY = Math.max(0.25, yNorm);
            double dirZ = radiusAtY * Math.sin(theta);
            double speed = 1.5 + level.getRandom().nextDouble() * 0.8;
            MagmaBombEntity bomb = new MagmaBombEntity(ModEntities.MAGMA_BOMB.get(), level);
            bomb.setPos(originX, originY, originZ);
            bomb.setDeltaMovement(dirX * speed, dirY * speed, dirZ * speed);
            level.addFreshEntity(bomb);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.ticksExisted = input.getIntOr("TicksExisted", this.ticksExisted);
        this.coneHeight = input.getIntOr("ConeHeight", this.coneHeight);
        this.baseRadius = input.getIntOr("BaseRadius", this.baseRadius);
        this.ventingTicks = input.getIntOr("VentingTicks", this.ventingTicks);
        this.ticksPerLayer = input.getIntOr("TicksPerLayer", this.ticksPerLayer);
        this.eruptionTicks = input.getIntOr("EruptionTicks", this.eruptionTicks);
        this.totalFuse = input.getIntOr("TotalFuse", this.totalFuse);
        if (input.getInt("OriginX").isPresent()) {
            this.originPos = new BlockPos(input.getIntOr("OriginX", 0), input.getIntOr("OriginY", 0), input.getIntOr("OriginZ", 0));
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("TicksExisted", this.ticksExisted);
        output.putInt("ConeHeight", this.coneHeight);
        output.putInt("BaseRadius", this.baseRadius);
        output.putInt("VentingTicks", this.ventingTicks);
        output.putInt("TicksPerLayer", this.ticksPerLayer);
        output.putInt("EruptionTicks", this.eruptionTicks);
        output.putInt("TotalFuse", this.totalFuse);

        if (this.originPos != null) {
            output.putInt("OriginX", this.originPos.getX());
            output.putInt("OriginY", this.originPos.getY());
            output.putInt("OriginZ", this.originPos.getZ());
        }
    }
}