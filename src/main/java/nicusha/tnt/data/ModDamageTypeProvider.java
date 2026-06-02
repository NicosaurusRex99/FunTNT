package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import nicusha.tnt.FunTNT;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDamageTypeProvider extends DatapackBuiltinEntriesProvider {

    public static final ResourceKey<DamageType> NUKE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(FunTNT.MODID, "nuke"));

    public ModDamageTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap).add(Registries.JUKEBOX_SONG, ModJukeboxSongProvider::bootstrap), Set.of(FunTNT.MODID));
    }

    private static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(NUKE, new DamageType(FunTNT.MODID + ".nuke", DamageScaling.ALWAYS, 0.1F));
    }
}