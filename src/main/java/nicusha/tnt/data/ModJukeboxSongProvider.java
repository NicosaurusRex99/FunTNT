package nicusha.tnt.data;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.JukeboxSong;
import nicusha.tnt.registry.ModItems;
import nicusha.tnt.registry.ModSounds;

public class ModJukeboxSongProvider {

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        context.register(ModItems.PARTY_BLAST_SONG, new JukeboxSong(Holder.direct(ModSounds.PARTY_BLAST.get()), Component.translatable("item.fun_tnt.party_music_disc.desc"), 110.0F, 15));
    }
}