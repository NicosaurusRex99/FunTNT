package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;
import nicusha.tnt.entities.BabyBoomerEntity;
import nicusha.tnt.entities.DynamiteEntity;
import nicusha.tnt.entities.NukeEntity;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, FunTNT.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BabyBoomerEntity>> BABY_BOOMER = ENTITIES.register("baby_boomer", () -> EntityType.Builder.<BabyBoomerEntity>of(BabyBoomerEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(64).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(FunTNT.MODID, "baby_boomer"))));
    public static final DeferredHolder<EntityType<?>, EntityType<NukeEntity>> NUKE = ENTITIES.register("nuke", () -> EntityType.Builder.<NukeEntity>of(NukeEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(64).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(FunTNT.MODID, "nuke"))));
    public static final DeferredHolder<EntityType<?>, EntityType<DynamiteEntity>> DYNAMITE = ENTITIES.register("dynamite", () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC).sized(0.1F, 0.1F).clientTrackingRange(64).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(FunTNT.MODID, "dynamite"))));

}
