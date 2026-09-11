package nicusha.tnt.entities.tnt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import nicusha.tnt.Config;
import nicusha.tnt.blocks.PaintTntBlock;
import nicusha.tnt.registry.ModEntities;
import org.jspecify.annotations.Nullable;

public class PaintTntEntity extends PrimedTnt {
    private static final EntityDataAccessor<Integer> DATA_PAINT_COLOR = SynchedEntityData.defineId(PaintTntEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE = SynchedEntityData.defineId(PaintTntEntity.class, EntityDataSerializers.BLOCK_STATE);
    public PaintTntEntity(EntityType<? extends PrimedTnt> type, Level level) {
        super(type, level);
    }

    public PaintTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity igniter) {
        super(ModEntities.PAINT.get(), level);
        this.setPos(x, y, z);
        double d0 = level.getRandom().nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
        this.setFuse(Config.PAINT_FUSE.get());
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = EntityReference.of(igniter);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PAINT_COLOR, DyeColor.WHITE.getId());
        builder.define(DATA_BLOCK_STATE, Blocks.TNT.defaultBlockState());
    }

    public void setPaintColor(DyeColor color) {
        this.entityData.set(DATA_PAINT_COLOR, color.getId());
    }

    public DyeColor getPaintColor() {
        return DyeColor.byId(this.entityData.get(DATA_PAINT_COLOR));
    }

    public void setStoredBlockState(BlockState state) {
        this.entityData.set(DATA_BLOCK_STATE, state);
        if (state.hasProperty(PaintTntBlock.COLOR)) {
            this.setPaintColor(state.getValue(PaintTntBlock.COLOR));
        }
    }

    public BlockState getStoredBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("PaintColor", this.getPaintColor().getId());
        nbt.store("StoredState", BlockState.CODEC, this.getStoredBlockState());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.getString("PaintColor").isPresent()) {
            this.setPaintColor(DyeColor.byId(nbt.getInt("PaintColor").get()));
        }
        nbt.read("StoredState", BlockState.CODEC).ifPresent(this::setStoredBlockState);
    }

    @Override
    protected void explode() {
        if (this.level().isClientSide()) return;
        BlockPos center = this.blockPosition();
        int radius = Config.PAINT_RADIUS.getAsInt();
        RandomSource random = this.level().getRandom();
        DyeColor currentPaint = this.getPaintColor();
        for (BlockPos targetPos : BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))) {
            double distance = Math.sqrt(targetPos.distSqr(center));
            double chance = 1.0 - (distance / (radius + 1));
            if (random.nextDouble() < chance) {
                applyPaint(targetPos, currentPaint);
            }
        }
        this.discard();
    }

    private void applyPaint(BlockPos pos, DyeColor colorToUse) {
        BlockState state = this.level().getBlockState(pos);
        Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = id.getPath();
        String namespace = id.getNamespace();

        for (DyeColor color : DyeColor.values()) {
            String prefix = color.getName() + "_";
            if (path.startsWith(prefix)) {
                String suffix = path.substring(prefix.length());
                Identifier newLoc = Identifier.fromNamespaceAndPath(namespace, colorToUse.getName() + "_" + suffix);

                BuiltInRegistries.BLOCK.getOptional(newLoc).ifPresent(holder -> {
                    Block newBlock = holder;
                    if (newBlock != Blocks.AIR) {
                        this.level().setBlock(pos, newBlock.withPropertiesOf(state), 3);
                    }
                });
                return;
            }
        }
    }
}