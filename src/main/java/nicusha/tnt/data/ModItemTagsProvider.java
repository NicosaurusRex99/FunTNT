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
        tag(ItemTags.CREEPER_IGNITERS).add(ModItems.DYNAMITE.get());
        tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModItems.PARTY_MUSIC_DISC.get());

        var explosives = commonTag("explosives");
        var tnt = commonTag("tnt");
        var ice = commonTag("ice");
        var musicDiscs = commonTag("music_discs");

        tag(explosives).add(ModBlocks.BABY_BOOMER.asItem(), ModBlocks.NUKE.asItem(), ModItems.DYNAMITE.get(), ModBlocks.CRYO.asItem(), ModBlocks.GRAVITY.asItem(), ModBlocks.PAINT.asItem(), ModBlocks.PARTY.asItem(), ModBlocks.THERMAL_FORGE.asItem(), ModBlocks.VOLCANO.asItem());
        tag(tnt).add(ModBlocks.BABY_BOOMER.asItem(), ModBlocks.NUKE.asItem(), ModBlocks.FERTILIZER.asItem(), ModBlocks.CRYO.asItem(), ModBlocks.GRAVITY.asItem(), ModBlocks.PAINT.asItem(), ModBlocks.PARTY.asItem(), ModBlocks.VOLCANO.asItem());
        tag(ice).add(Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE);
        tag(musicDiscs).add(ModItems.PARTY_MUSIC_DISC.get());
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