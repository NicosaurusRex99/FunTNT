package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import nicusha.tnt.registry.ModEntities;

import javax.annotation.Nullable;

public class FertilizerEntity extends PrimedTnt {
    public FertilizerEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public FertilizerEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.FERTILIZER.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(igniter);
    }

    @Override
    protected void explode() {
        if (this.level() instanceof ServerLevel serverLevel) {
            int radius = 10;
            BlockPos center = this.blockPosition();
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 2.0F, 0.5F);
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
                if (pos.distSqr(center) <= radius * radius) {
                    BlockState state = serverLevel.getBlockState(pos);
                    Block block = state.getBlock();
                    if (state.is(BlockTags.DIRT)) {
                        serverLevel.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                        state = serverLevel.getBlockState(pos);
                    }

                    if (block instanceof BonemealableBlock bonemealable) {
                        if (bonemealable.isValidBonemealTarget(serverLevel, pos, state)) {
                            bonemealable.performBonemeal(serverLevel, serverLevel.getRandom(), pos, state);
                        }
                    }

                    BlockPos abovePos = pos.above();
                    if (serverLevel.isEmptyBlock(abovePos)) {
                        if (serverLevel.getBlockState(pos).is(BlockTags.SUPPORTS_VEGETATION)) {
                            float chance = serverLevel.getRandom().nextFloat();
                            if (chance < 0.1F) {
                                serverLevel.registryAccess().lookup(Registries.BLOCK).flatMap(reg -> reg.get(BlockTags.SMALL_FLOWERS)).flatMap(tag -> tag.getRandomElement(serverLevel.getRandom())).ifPresent(flowerHolder -> {serverLevel.setBlock(abovePos, flowerHolder.value().defaultBlockState(), 3);});
                            } else if (chance < 0.3F) {
                                serverLevel.setBlock(abovePos, Blocks.SHORT_GRASS.defaultBlockState(), 3);
                            }
                        }
                        else if (serverLevel.getBlockState(pos).is(BlockTags.SUPPORTS_DRY_VEGETATION)) {
                            float sandChance = serverLevel.getRandom().nextFloat();
                            if (sandChance < 0.05F) {
                                if (Blocks.CACTUS.defaultBlockState().canSurvive(serverLevel, abovePos)) {
                                    serverLevel.setBlock(abovePos, Blocks.CACTUS.defaultBlockState(), 3);
                                }
                            } else if (sandChance < 0.15F) {
                                serverLevel.setBlock(abovePos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(), this.getZ(), 100, 5, 2, 5, 0.2);
            serverLevel.sendParticles(ParticleTypes.COMPOSTER, this.getX(), this.getY(), this.getZ(), 50, 3, 1, 3, 0.1);
        }
        this.discard();
    }
}