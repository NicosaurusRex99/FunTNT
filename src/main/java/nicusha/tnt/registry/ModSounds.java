package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, FunTNT.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PARTY_BLAST = SOUND_EVENTS.register("music_disc.party_blast", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FunTNT.MODID, "music_disc.party_blast")));
}