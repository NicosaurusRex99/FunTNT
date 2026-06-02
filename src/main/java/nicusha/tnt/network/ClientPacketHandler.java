package nicusha.tnt.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nicusha.tnt.network.payload.SpinPlayerPayload;

public class ClientPacketHandler {

    public static void handleSpin(final SpinPlayerPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null && !mc.isPaused()) {
                float newYRot = (player.getYRot() + payload.degrees()) % 360.0F;

                player.setYRot(newYRot);
                player.setYHeadRot(newYRot);
                player.setYBodyRot(newYRot);

                player.yRotO += payload.degrees();
                player.yHeadRotO += payload.degrees();
                player.yBodyRotO += payload.degrees();
            }
        });
    }
}