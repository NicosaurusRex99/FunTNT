package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FunTNT.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.CREEPER_IGNITERS).add(ModItems.DYNAMITE.getKey());
        tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModItems.PARTY_MUSIC_DISC.getKey());

        var explosives = commonTag("explosives");
        var tnt = commonTag("tnt");
        var ice = commonTag("ice");
        var musicDiscs = commonTag("music_discs");

        tag(explosives).add(ModBlocks.BABY_BOOMER.asItem().builtInRegistryHolder().getKey(), ModBlocks.NUKE.asItem().builtInRegistryHolder().key(), ModItems.DYNAMITE.get().builtInRegistryHolder().key(), ModBlocks.CRYO.asItem().builtInRegistryHolder().key(), ModBlocks.GRAVITY.asItem().builtInRegistryHolder().key(), ModBlocks.PAINT.asItem().builtInRegistryHolder().key(), ModBlocks.PARTY.asItem().builtInRegistryHolder().key(), ModBlocks.THERMAL_FORGE.asItem().builtInRegistryHolder().key(), ModBlocks.VOLCANO.asItem().builtInRegistryHolder().key(), ModBlocks.RIFT.asItem().builtInRegistryHolder().key());
        tag(tnt).add(ModBlocks.BABY_BOOMER.asItem().builtInRegistryHolder().key(), ModBlocks.NUKE.asItem().builtInRegistryHolder().key(), ModBlocks.FERTILIZER.asItem().builtInRegistryHolder().key(), ModBlocks.CRYO.asItem().builtInRegistryHolder().key(), ModBlocks.GRAVITY.asItem().builtInRegistryHolder().key(), ModBlocks.PAINT.asItem().builtInRegistryHolder().key(), ModBlocks.PARTY.asItem().builtInRegistryHolder().key(), ModBlocks.VOLCANO.asItem().builtInRegistryHolder().key(), ModBlocks.RIFT.asItem().builtInRegistryHolder().key());
        tag(ice).add(Items.ICE.builtInRegistryHolder().key(), Items.BLUE_ICE.builtInRegistryHolder().key(), Items.PACKED_ICE.builtInRegistryHolder().key());
        tag(musicDiscs).add(ModItems.PARTY_MUSIC_DISC.getKey());
    }

    private TagKey<Item> commonTag(String name){
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", name));
    }
    private TagKey<Item> minecraftTag(String name){
        return TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace(name));
    }
    private TagKey<Item> customTag(String name){
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FunTNT.MODID, name));
    }
}