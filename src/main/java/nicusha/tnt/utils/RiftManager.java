package nicusha.tnt.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class RiftManager {

    public record BlockData(BlockState state, @Nullable CompoundTag nbt) {}
    private record PendingRift(BlockPos center, int radius, long expiresAtTick) {}

    private static final Map<ResourceKey<Level>, PendingRift> PENDING = new HashMap<>();

    public static void trigger(ServerLevel level, BlockPos center, int radius, int timeoutTicks) {
        ResourceKey<Level> dimension = level.dimension();
        PendingRift partner = PENDING.get(dimension);
        if (partner == null || level.getGameTime() > partner.expiresAtTick() || partner.radius() != radius) {
            PENDING.put(dimension, new PendingRift(center.immutable(), radius, level.getGameTime() + timeoutTicks));
            spawnArmFx(level, center, radius);
            return;
        }
        PENDING.remove(dimension);
        BlockPos posA = partner.center();
        BlockPos posB = center.immutable();
        if (!isRegionLoaded(level, posA, radius) || !isRegionLoaded(level, posB, radius)) {
            spawnFailFx(level, posB);
            return;
        }
        BlockData[] dataA = capture(level, posA, radius);
        BlockData[] dataB = capture(level, posB, radius);
        swapEntities(level, posA, posB, radius);
        place(level, posA, radius, dataB);
        place(level, posB, radius, dataA);
        spawnSwapFx(level, posA, radius);
        spawnSwapFx(level, posB, radius);
    }

    private static boolean isRegionLoaded(ServerLevel level, BlockPos center, int radius) {
        BlockPos min = center.offset(-radius, -radius, -radius);
        BlockPos max = center.offset(radius, radius, radius);
        return level.hasChunksAt(min, max);
    }

    private static BlockData[] capture(ServerLevel level, BlockPos center, int radius) {
        int size = radius * 2 + 1;
        BlockData[] data = new BlockData[size * size * size];
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int i = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockState state = level.getBlockState(pos);
                    if (state.getDestroySpeed(level, pos) < 0) {
                        data[i++] = null;
                        continue;
                    }
                    CompoundTag nbt = null;
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be != null) {
                        nbt = be.saveWithFullMetadata(level.registryAccess());
                    }
                    data[i++] = new BlockData(state, nbt);
                }
            }
        }
        return data;
    }

    private static void place(ServerLevel level, BlockPos center, int radius, BlockData[] data) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int i = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockData bd = data[i++];
                    if (bd != null && level.getBlockEntity(pos) != null) {
                        level.removeBlockEntity(pos);
                    }
                }
            }
        }
        i = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    pos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    BlockData bd = data[i++];
                    if (bd == null) continue;
                    level.setBlock(pos, bd.state(), 3);
                    if (bd.nbt() != null) {
                        BlockEntity be = BlockEntity.loadStatic(pos, bd.state(), bd.nbt(), level.registryAccess());
                        if (be != null) {
                            level.setBlockEntity(be);
                        }
                    }
                }
            }
        }
    }

    private static void swapEntities(ServerLevel level, BlockPos posA, BlockPos posB, int radius) {
        AABB boundsA = new AABB(posA).inflate(radius + 0.5);
        AABB boundsB = new AABB(posB).inflate(radius + 0.5);
        List<Entity> entitiesA = level.getEntitiesOfClass(Entity.class, boundsA, e -> !e.isSpectator() && !e.isPassenger());
        List<Entity> entitiesB = level.getEntitiesOfClass(Entity.class, boundsB, e -> !e.isSpectator() && !e.isPassenger());
        Vec3 delta = Vec3.atLowerCornerOf(posB.subtract(posA));
        teleportGroup(entitiesA, delta);
        teleportGroup(entitiesB, delta.scale(-1));
    }

    private static void teleportGroup(List<Entity> entities, Vec3 delta) {
        for (Entity e : entities) {
            Vec3 originalVelocity = e.getDeltaMovement();
            Vec3 targetPos = e.position().add(delta);

            e.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            e.setDeltaMovement(originalVelocity);
            for (Entity passenger : e.getIndirectPassengers()) {
                passenger.setDeltaMovement(originalVelocity);
            }
        }
    }

    private static void spawnArmFx(ServerLevel level, BlockPos pos, int radius) {
        level.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 80, radius, radius, radius, 0.15D);
        level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 0.6F);
    }

    private static void spawnSwapFx(ServerLevel level, BlockPos pos, int radius) {
        level.sendParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 120, radius, radius, radius, 0.2D);
        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, 0.8F);
    }

    private static void spawnFailFx(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 0.5, 0.5, 0.5, 0.05D);
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}