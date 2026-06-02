package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;
import nicusha.tnt.item.DynamiteItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FunTNT.MODID);

    public static final ResourceKey<JukeboxSong> PARTY_BLAST_SONG = ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(FunTNT.MODID, "party_blast"));

    public static final DeferredItem<Item> DYNAMITE = ITEMS.register("dynamite", () -> new DynamiteItem(new Item.Properties().useCooldown(0.4F).setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FunTNT.MODID, "dynamite")))));
    public static final DeferredItem<Item> PARTY_MUSIC_DISC = ITEMS.register("party_blast", () -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FunTNT.MODID, "party_blast"))).jukeboxPlayable(PARTY_BLAST_SONG).stacksTo(1).rarity(Rarity.EPIC)));


}