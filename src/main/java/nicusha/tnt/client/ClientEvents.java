package nicusha.tnt.client;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import nicusha.tnt.client.renderer.GenericTntRenderer;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;

public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NUKE.get(), context -> new GenericTntRenderer(context, ModBlocks.NUKE));
        event.registerEntityRenderer(ModEntities.FERTILIZER.get(), context -> new GenericTntRenderer(context, ModBlocks.FERTILIZER));
        event.registerEntityRenderer(ModEntities.BABY_BOOMER.get(), context -> new GenericTntRenderer(context, ModBlocks.BABY_BOOMER));
        event.registerEntityRenderer(ModEntities.CRYO.get(), context -> new GenericTntRenderer(context, ModBlocks.CRYO));
        event.registerEntityRenderer(ModEntities.GRAVITY.get(), context -> new GenericTntRenderer(context, ModBlocks.GRAVITY));

        event.registerEntityRenderer(ModEntities.DYNAMITE.get(), ThrownItemRenderer::new);
    }

}
