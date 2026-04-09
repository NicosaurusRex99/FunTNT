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
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ItemTags.CREEPER_IGNITERS).add(ModItems.DYNAMITE.get());

        var explosives = commonTag("explosives");
        var tnt = commonTag("tnt");
        var ice = commonTag("ice");

        tag(explosives).add(ModBlocks.BABY_BOOMER.asItem()).add(ModBlocks.NUKE.asItem()).add(ModItems.DYNAMITE.get()).add(ModBlocks.CRYO.asItem()).add(ModBlocks.GRAVITY.asItem());
        tag(tnt).add(ModBlocks.BABY_BOOMER.asItem()).add(ModBlocks.NUKE.asItem()).add(ModBlocks.FERTILIZER.asItem()).add(ModBlocks.CRYO.asItem()).add(ModBlocks.GRAVITY.asItem());
        tag(ice).add(Items.ICE).add(Items.BLUE_ICE).add(Items.PACKED_ICE);
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