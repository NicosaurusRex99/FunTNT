package nicusha.tnt;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import nicusha.tnt.client.ClientEvents;
import nicusha.tnt.data.DataGenerators;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModCreativeTabs;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(FunTNT.MODID)
public class FunTNT {
    public static final String MODID = "fun_tnt";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FunTNT(IEventBus bus, Dist dist) {
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModCreativeTabs.TABS.register(bus);
        ModEntities.ENTITIES.register(bus);

        bus.addListener(this::addCreative);
        bus.addListener(DataGenerators::genData);

        if (FMLEnvironment.getDist().isClient()) {
            bus.addListener(ClientEvents::registerRenderers);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeTabs.FUN_TNT_TAB.getKey()) {
            ModItems.ITEMS.getEntries().forEach(item -> event.accept(item.get()));
        }
    }

}