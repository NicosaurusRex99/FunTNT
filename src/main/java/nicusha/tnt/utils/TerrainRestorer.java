package nicusha.tnt.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;

public class TerrainRestorer {

    public static void restoreArea(ServerLevel level, BlockPos centerPos, int radius) {
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        RandomState randomState = level.getChunkSource().randomState();
        int rSq = radius * radius;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int worldX = centerPos.getX() + x;
                int worldZ = centerPos.getZ() + z;
                NoiseColumn column = generator.getBaseColumn(worldX, worldZ, level, randomState);
                for (int y = -radius; y <= radius; y++) {
                    int worldY = centerPos.getY() + y;
                    if (x * x + y * y + z * z <= rSq) {
                        mutablePos.set(worldX, worldY, worldZ);
                        BlockState currentState = level.getBlockState(mutablePos);
                        if (currentState.isAir() || currentState.canBeReplaced()) {
                            BlockState seedState = column.getBlock(worldY);
                            BlockState adjustedState = getBlendedMaterial(level, mutablePos, seedState);
                            if (!adjustedState.isAir()) {
                                level.setBlock(mutablePos, adjustedState, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    private static BlockState getBlendedMaterial(ServerLevel level, BlockPos pos, BlockState seedState) {
        int dirtNeighbors = 0;
        int stoneNeighbors = 0;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(dir));
            if (neighbor.is(BlockTags.DIRT) || neighbor.is(Blocks.GRASS_BLOCK)) {
                dirtNeighbors++;
            } else if (neighbor.is(BlockTags.BASE_STONE_OVERWORLD)) {
                stoneNeighbors++;
            }
        }
        if (seedState.is(BlockTags.BASE_STONE_OVERWORLD) && dirtNeighbors >= 2) {
            return Blocks.DIRT.defaultBlockState();
        }
        if (seedState.isAir() && stoneNeighbors >= 3) {
            return Blocks.STONE.defaultBlockState();
        }

        return seedState;
    }
}