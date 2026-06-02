package nicusha.tnt;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import nicusha.tnt.client.ClientEvents;
import nicusha.tnt.data.DataGenerators;
import nicusha.tnt.network.NetworkingRegistration;
import nicusha.tnt.registry.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FunTNT.MODID)
public class FunTNT {
    public static final String MODID = "fun_tnt";

    public FunTNT(IEventBus bus, ModContainer container) {
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModCreativeTabs.TABS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModSounds.SOUND_EVENTS.register(bus);
        ModEffects.MOB_EFFECTS.register(bus);
        bus.addListener(this::addCreative);
        bus.addListener(DataGenerators::genData);
        if (FMLEnvironment.getDist().isClient()) {
            bus.addListener(ClientEvents::registerRenderers);
            bus.addListener(ClientEvents::registerBlockColors);
        }
        bus.addListener(NetworkingRegistration::registerPackets);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeTabs.FUN_TNT_TAB.getKey()) {
            ModItems.ITEMS.getEntries().forEach(item -> event.accept(item.get()));
        }
    }

}