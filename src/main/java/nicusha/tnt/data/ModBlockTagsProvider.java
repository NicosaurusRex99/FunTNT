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
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.FERTILIZER.get()).add(ModBlocks.CRYO.get()).add(ModBlocks.GRAVITY.get()).add(ModBlocks.RESTORATION.get()).add(ModBlocks.LATELY.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NUKE.get()).add(ModBlocks.TRIP_MINE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.NUKE.get()).add(ModBlocks.TRIP_MINE.get());

        var tnt = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "tnt"));
        this.tag(tnt).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get()).add(ModBlocks.FERTILIZER.get()).add(ModBlocks.GRAVITY.get()).add(ModBlocks.TRIP_MINE.get()).add(ModBlocks.RESTORATION.get()).add(ModBlocks.LATELY.get());
    }
}