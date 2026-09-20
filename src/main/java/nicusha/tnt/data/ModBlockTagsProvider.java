package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FunTNT.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.BABY_BOOMER.getKey(), ModBlocks.FERTILIZER.getKey(), ModBlocks.CRYO.getKey(), ModBlocks.GRAVITY.getKey(), ModBlocks.RESTORATION.getKey(), ModBlocks.LATELY.getKey(), ModBlocks.PAINT.getKey(), ModBlocks.PARTY.getKey(), ModBlocks.RIFT.getKey());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NUKE.getKey(), ModBlocks.TRIP_MINE.getKey(), ModBlocks.THERMAL_FORGE.getKey(), ModBlocks.VOLCANO.getKey());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.NUKE.getKey(), ModBlocks.TRIP_MINE.getKey(), ModBlocks.THERMAL_FORGE.getKey());

        var tnt = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "tnt"));
        this.tag(tnt).add(ModBlocks.BABY_BOOMER.getKey(), ModBlocks.NUKE.getKey(), ModBlocks.FERTILIZER.getKey(), ModBlocks.GRAVITY.getKey(), ModBlocks.TRIP_MINE.getKey(), ModBlocks.RESTORATION.getKey(), ModBlocks.LATELY.getKey(), ModBlocks.PAINT.getKey(), ModBlocks.PARTY.getKey(), ModBlocks.THERMAL_FORGE.getKey(), ModBlocks.VOLCANO.getKey(), ModBlocks.RIFT.getKey());
    }
}