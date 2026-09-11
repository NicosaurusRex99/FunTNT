package nicusha.tnt.entities.tnt;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nicusha.tnt.Config;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModEntities;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class LatelyEntity extends PrimedTnt {
    public LatelyEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public LatelyEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.LATELY.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(Config.FERTILIZER_FUSE.get());
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(igniter);
    }

    @Override
    protected void explode() {
        if (this.level().isClientSide()) return;
        BlockPos pos = this.blockPosition();
        this.level().playSound(null, pos, SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.level().playSound(null, pos, SoundEvents.CAT_PURREOW_BABY.value(), SoundSource.NEUTRAL, 2.0F, 0.8F);
        AABB explosionArea = new AABB(pos).inflate(8.0D);
        List<Player> players = this.level().getEntitiesOfClass(Player.class, explosionArea);
        for (Player player : players) {
            player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 160, 0));
            player.sendOverlayMessage(Component.translatable(FunTNT.MODID + ".sly_withers_lately.reference").withStyle(ChatFormatting.ITALIC));
        }
        var variantRegistry = this.level().registryAccess().lookupOrThrow(Registries.CAT_VARIANT);
        for (int i = 0; i < 25; i++) {
            Cat cat = EntityType.CAT.create(this.level(), EntitySpawnReason.EVENT);
            if (cat != null) {
                cat.setPos(position());
                cat.setYHeadRot(this.level().getRandom().nextFloat() * 360.0F);
                cat.setVariant(variantRegistry.getRandom(level().getRandom()).get());
                double d0 = this.level().getRandom().nextDouble() * 0.5D + 0.2D;
                double d1 = this.level().getRandom().nextDouble() * 2.0D * Math.PI;
                cat.setDeltaMovement(Math.cos(d1) * d0, 0.7D, Math.sin(d1) * d0);
                this.level().addFreshEntity(cat);
            }
        }
        this.discard();
    }
}