package nicusha.tnt.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class BaseCustomTntBlock extends TntBlock {
    private final TntFactory<? extends PrimedTnt> entityFactory;
    private final int fuseTime;

    public BaseCustomTntBlock(Properties properties, TntFactory<? extends PrimedTnt> entityFactory, int fuseTime) {
        super(properties);
        this.entityFactory = entityFactory;
        this.fuseTime = fuseTime;
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.FALSE));
    }

    @Override
    public boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
                PrimedTnt tnt = entityFactory.create(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
                tnt.setFuse((short)this.fuseTime);
                level.addFreshEntity(tnt);
                level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.getBestNeighborSignal(pos) > 0) {
                if (this.onCaughtFire(state, level, pos, null, null)) {
                    level.removeBlock(pos, false);
                }
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.getBestNeighborSignal(pos) > 0 || level.hasNeighborSignal(pos)) {
            if (this.onCaughtFire(state, level, pos, null, null)) {
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());
        if (itemstack.is(Items.FLINT_AND_STEEL) || itemstack.is(Items.FIRE_CHARGE)) {
            if (this.onCaughtFire(state, level, pos, hitResult.getDirection(), player)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                itemstack.hurtAndBreak(1, player, player.getUsedItemHand());
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (level instanceof Level realLevel && !realLevel.isClientSide()) {
            if (neighbourState.isSignalSource()) {
                boolean isActive = false;

                for (Direction dir : Direction.values()) {
                    if (neighbourState.getSignal(realLevel, neighbourPos, dir) > 0) {
                        isActive = true;
                        break;
                    }
                }

                if (isActive) {
                    if (this.onCaughtFire(state, realLevel, pos, directionToNeighbour, null)) {
                        realLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    public int getFuseTime() {
        return fuseTime;
    }

    public TntFactory<? extends PrimedTnt> getEntityFactory() {
        return entityFactory;
    }

    @FunctionalInterface
    public interface TntFactory<T extends PrimedTnt> {
        T create(Level level, double x, double y, double z, @Nullable LivingEntity igniter);
    }
}