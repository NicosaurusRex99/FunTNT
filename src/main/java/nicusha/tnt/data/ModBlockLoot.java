package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import nicusha.tnt.registry.ModBlocks;

import java.util.Collections;

public class ModBlockLoot extends BlockLootSubProvider {

    protected ModBlockLoot(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BABY_BOOMER.get());
        dropSelf(ModBlocks.NUKE.get());
        dropSelf(ModBlocks.FERTILIZER.get());
        dropSelf(ModBlocks.CRYO.get());
        dropSelf(ModBlocks.GRAVITY.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
    }
}