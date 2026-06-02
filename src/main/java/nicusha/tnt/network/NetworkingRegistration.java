package nicusha.tnt.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import nicusha.tnt.FunTNT;
import nicusha.tnt.network.payload.SpinPlayerPayload;

public class NetworkingRegistration {

    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FunTNT.MODID).versioned("1.0.0");
        registrar.playToClient(SpinPlayerPayload.TYPE, SpinPlayerPayload.CODEC, ClientPacketHandler::handleSpin);
    }
}
