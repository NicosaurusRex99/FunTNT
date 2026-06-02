package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.effect.MobEffect;
import nicusha.tnt.FunTNT;
import nicusha.tnt.effect.PartyDanceEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, FunTNT.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> PARTY_DANCE = MOB_EFFECTS.register("party_dance", PartyDanceEffect::new);
}