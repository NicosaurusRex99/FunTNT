package nicusha.tnt.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import nicusha.tnt.entities.PaintTntEntity;

import javax.annotation.Nullable;

public class PaintTntBlock extends BaseCustomTntBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public PaintTntBlock(Properties properties, TntFactory<?> entityFactory, int fuseTime) {
        super(properties, entityFactory, fuseTime);
        this.registerDefaultState(this.defaultBlockState().setValue(COLOR, DyeColor.WHITE).setValue(UNSTABLE, Boolean.FALSE));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        DyeColor dyeColor = DyeColor.getColor(stack);
        if (dyeColor != null) {
            if (state.getValue(COLOR) != dyeColor) {
                if (!level.isClientSide()) {
                    level.setBlock(pos, state.setValue(COLOR, dyeColor), 3);
                    stack.consume(1, player);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
                var tnt = getEntityFactory().create(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
                if (tnt instanceof PaintTntEntity paintTnt) {
                    paintTnt.setStoredBlockState(state);
                }
                tnt.setFuse((short)this.getFuseTime());
                level.addFreshEntity(tnt);
                return true;
            }
        }
        return false;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }
}