package nicusha.tnt.client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import nicusha.tnt.FunTNT;
import nicusha.tnt.blocks.PaintTntBlock;
import nicusha.tnt.client.renderer.GenericTntRenderer;
import nicusha.tnt.client.renderer.PaintTntEntityRenderer;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;

import java.util.List;
import java.util.Set;

public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NUKE.get(), context -> new GenericTntRenderer(context, ModBlocks.NUKE));
        event.registerEntityRenderer(ModEntities.FERTILIZER.get(), context -> new GenericTntRenderer(context, ModBlocks.FERTILIZER));
        event.registerEntityRenderer(ModEntities.BABY_BOOMER.get(), context -> new GenericTntRenderer(context, ModBlocks.BABY_BOOMER));
        event.registerEntityRenderer(ModEntities.CRYO.get(), context -> new GenericTntRenderer(context, ModBlocks.CRYO));
        event.registerEntityRenderer(ModEntities.GRAVITY.get(), context -> new GenericTntRenderer(context, ModBlocks.GRAVITY));
        event.registerEntityRenderer(ModEntities.RESTORATION.get(), context -> new GenericTntRenderer(context, ModBlocks.RESTORATION));
        event.registerEntityRenderer(ModEntities.LATELY.get(), context -> new GenericTntRenderer(context, ModBlocks.LATELY));
        event.registerEntityRenderer(ModEntities.PAINT.get(), context -> new PaintTntEntityRenderer(context));
        event.registerEntityRenderer(ModEntities.PARTY.get(), context -> new GenericTntRenderer(context, ModBlocks.PARTY));
        event.registerEntityRenderer(ModEntities.THERMAL_FORGE.get(), context -> new GenericTntRenderer(context, ModBlocks.THERMAL_FORGE));

        event.registerEntityRenderer(ModEntities.DYNAMITE.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        BlockTintSource paintSource = new BlockTintSource() {
            @Override
            public int color(net.minecraft.world.level.block.state.BlockState state) {
                if (state.hasProperty(PaintTntBlock.COLOR)) {
                    return state.getValue(PaintTntBlock.COLOR).getTextureDiffuseColor();
                }
                return -1;
            }
            @Override
            public Set<Property<?>> relevantProperties() {
                return Set.of(PaintTntBlock.COLOR);
            }
        };
        event.register(List.of(paintSource), ModBlocks.PAINT.get());
    }

    @SubscribeEvent
    public static void onItemTooltip(final ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof BlockItem) {
            Identifier registryName = BuiltInRegistries.ITEM.getKey(item);
            if (registryName != null && registryName.getNamespace().equals(FunTNT.MODID)) {
                String blockId = registryName.getPath();
                String tooltipKey = "tooltip." + FunTNT.MODID + "." + blockId + ".description";
                event.getToolTip().add(Component.translatable(tooltipKey).withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }
    }
}
