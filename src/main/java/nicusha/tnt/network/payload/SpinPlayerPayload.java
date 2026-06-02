package nicusha.tnt.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import nicusha.tnt.FunTNT;

public record SpinPlayerPayload(float degrees) implements CustomPacketPayload {

    public static final Type<SpinPlayerPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(FunTNT.MODID, "spin_player"));

    public static final StreamCodec<FriendlyByteBuf, SpinPlayerPayload> CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, SpinPlayerPayload::degrees, SpinPlayerPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}