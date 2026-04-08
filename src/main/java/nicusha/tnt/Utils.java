package nicusha.tnt;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import nicusha.tnt.data.ModDamageTypeProvider;

public class Utils {

    public static DamageSource nukeDamageSource(Level level, Entity nuke) {
        var registry = level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        var nukeType = registry.get(ModDamageTypeProvider.NUKE).orElseGet(() -> registry.getOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC));
        return new DamageSource(nukeType, nuke);
    }
}
