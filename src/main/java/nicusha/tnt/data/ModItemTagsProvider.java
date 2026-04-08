package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FunTNT.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.CREEPER_IGNITERS).add(ModBlocks.BABY_BOOMER.asItem()).add(ModBlocks.NUKE.asItem());

        var explosives = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "explosives"));
        var tnt = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tnt"));
        this.tag(explosives).add(ModBlocks.BABY_BOOMER.asItem()).add(ModBlocks.NUKE.asItem());
        this.tag(tnt).add(ModBlocks.BABY_BOOMER.asItem()).add(ModBlocks.NUKE.asItem());
    }
}