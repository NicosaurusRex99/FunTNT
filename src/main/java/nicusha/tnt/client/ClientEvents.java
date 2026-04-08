package nicusha.tnt.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import nicusha.tnt.client.renderer.BabyBoomerRenderer;
import nicusha.tnt.registry.ModEntities;

public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BABY_BOOMER.get(), BabyBoomerRenderer::new);
    }
}
