package nicusha.tnt.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import nicusha.tnt.entities.tnt.VolcanoEntity;
import nicusha.tnt.registry.ModEntities;

import javax.annotation.Nullable;

public class VolcanoTntBlock extends BaseCustomTntBlock {

    public VolcanoTntBlock(Properties properties, BaseCustomTntBlock.TntFactory<?> entityFactory, int fuseTime) {
        super(properties, entityFactory, fuseTime);
    }

    private static void primeVolcanoTnt(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!level.isClientSide()) {
            VolcanoEntity tnt = new VolcanoEntity(ModEntities.VOLCANO_TNT.get(), level);
            tnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            tnt.setOriginPos(pos);
            if (igniter != null) {
                tnt.owner = EntityReference.of(igniter);
            }
            level.addFreshEntity(tnt);
            level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(),
                    SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 0.8F);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock()) && level.hasNeighborSignal(pos)) {
            primeVolcanoTnt(level, pos, null);
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (level.hasNeighborSignal(pos)) {
            primeVolcanoTnt(level, pos, null);
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            primeVolcanoTnt(level, pos, player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);

            if (!player.isCreative()) {
                if (stack.is(Items.FLINT_AND_STEEL)) {
                    stack.hurtAndBreak(1, player, hand);
                } else {
                    stack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        super.wasExploded(level, pos, explosion);
        if (!level.isClientSide()) {
            VolcanoEntity tnt = new VolcanoEntity(ModEntities.VOLCANO_TNT.get(), level);
            tnt.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            tnt.setOriginPos(pos);
            level.addFreshEntity(tnt);
        }
    }
}
