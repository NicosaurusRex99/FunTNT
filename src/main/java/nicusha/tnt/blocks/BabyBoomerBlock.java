package nicusha.tnt.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import nicusha.tnt.entities.BabyBoomerEntity;

import javax.annotation.Nullable;

public class BabyBoomerBlock extends TntBlock {
    public BabyBoomerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
                BabyBoomerEntity babyBoomer = new BabyBoomerEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter);
                level.addFreshEntity(babyBoomer);
                level.playSound(null, babyBoomer.getX(), babyBoomer.getY(), babyBoomer.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
                return true;
            }
        }
        return false;
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            BabyBoomerEntity babyBoomer = new BabyBoomerEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
            int i = babyBoomer.getFuse();
            babyBoomer.setFuse((short)(level.getRandom().nextInt(i / 4) + i / 8));
            level.addFreshEntity(babyBoomer);
        }
    }
}