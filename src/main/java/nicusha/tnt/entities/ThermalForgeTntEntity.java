package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nicusha.tnt.Config;
import nicusha.tnt.registry.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ThermalForgeTntEntity extends PrimedTnt {

    public ThermalForgeTntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public ThermalForgeTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.THERMAL_FORGE.get(), level);
        this.setPos(x, y, z);
        this.setFuse(Config.THERMAL_FORGE_FUSE.get());
    }

    @Override
    protected void explode() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        BlockPos epicenter = this.blockPosition();
        int radius = Config.THERMAL_FORGE_RADIUS.get();
        serverLevel.playSound(null, epicenter, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 4.0F, 0.6F);
        serverLevel.playSound(null, epicenter, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 2.0F, 0.5F);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((x * x + y * y + z * z) > (radius * radius)) continue;
                    BlockPos targetPos = epicenter.offset(x, y, z);
                    BlockState targetState = serverLevel.getBlockState(targetPos);
                    if (!targetState.is(BlockTags.create(Identifier.fromNamespaceAndPath("c", "ores")))) continue;
                    Block targetBlock = targetState.getBlock();
                    if (targetState.isAir() || targetState.is(Blocks.BEDROCK)) continue;
                    ItemStack blockAsItem = new ItemStack(targetBlock.asItem());
                    if (blockAsItem.isEmpty()) continue;
                    Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(blockAsItem), serverLevel);
                    if (smeltingRecipe.isPresent()) {
                        ItemStack resultStack = smeltingRecipe.get().value().assemble(new SingleRecipeInput(blockAsItem)).copy();
                        if (!resultStack.isEmpty()) {
                            if (serverLevel.getRandom().nextFloat() < 0.33F) {
                                resultStack.setCount(resultStack.getCount() * 2);
                            }
                            serverLevel.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                            ItemEntity droppedIngot = new ItemEntity(serverLevel, targetPos.getX() + 0.5, targetPos.getY() + 0.2, targetPos.getZ() + 0.5, resultStack);
                            droppedIngot.setDeltaMovement(0.0, 0.1, 0.0);
                            serverLevel.addFreshEntity(droppedIngot);
                            serverLevel.sendParticles(ParticleTypes.LAVA, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.0);
                        }
                    }
                }
            }
        }
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, epicenter.getX() + 0.5, epicenter.getY() + 0.5, epicenter.getZ() + 0.5, 25, 1.0, 1.0, 1.0, 0.05);
        this.discard();
    }
}