package nicusha.tnt.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nicusha.tnt.Config;
import nicusha.tnt.registry.ModEffects;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;
import nicusha.tnt.registry.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartyTntEntity extends PrimedTnt {

    public PartyTntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public PartyTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.PARTY.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(Config.PARTY_FUSE.get());
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(igniter);
    }

    @Override
    protected void explode() {
        if (this.level().isClientSide()) return;
        BlockPos pos = this.blockPosition();
        this.level().playSound(null, pos, ModSounds.PARTY_BLAST.get(), SoundSource.RECORDS, 4.0F, 1.0F);

        AABB partyZone = new AABB(pos).inflate(Config.PARTY_RADIUS.get());
        List<LivingEntity> partyGoers = this.level().getEntitiesOfClass(LivingEntity.class, partyZone);

        for (LivingEntity mob : partyGoers) {
            mob.addEffect(new MobEffectInstance(ModEffects.PARTY_DANCE.getDelegate(), 2200, 0, false, false));
        }

        List<ItemEntity> droppedItems = this.level().getEntitiesOfClass(ItemEntity.class, partyZone);
        for (ItemEntity itemEntity : droppedItems) {
            if(itemEntity.getItem().is(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "music_discs")))) {
                itemEntity.setItem(ModItems.PARTY_MUSIC_DISC.toStack());
            }
        }

        this.discard();
    }
}