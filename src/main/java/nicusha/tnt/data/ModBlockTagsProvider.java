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
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.BABY_BOOMER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NUKE.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get());
        this.tag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get());
        this.tag(BlockTags.PREVENT_MOB_SPAWNING_INSIDE).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get());
        this.tag(BlockTags.AZALEA_ROOT_REPLACEABLE).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get());

        var tnt = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "tnt"));
        this.tag(tnt).add(ModBlocks.BABY_BOOMER.get()).add(ModBlocks.NUKE.get());
    }
}