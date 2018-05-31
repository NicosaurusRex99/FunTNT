package naturix.funtnt;

import org.apache.logging.log4j.Logger;

import naturix.funtnt.proxy.CommonProxy;
import naturix.funtnt.registry.ModBlocks;
import naturix.funtnt.registry.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = FunTNT.MODID, name = FunTNT.NAME, version = FunTNT.VERSION)
public class FunTNT
{
    public static final String MODID = "funtnt";
    public static final String NAME = "Fun TNT";
    public static final String VERSION = "1.12.2.0";

    @SidedProxy(clientSide = "naturix.funtnt.proxy.ClientProxy", serverSide = "naturix.funtnt.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static FunTNT instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        ModEntities.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {
	
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModBlocks.registerItemBlocks(event.getRegistry());
		}
		@SubscribeEvent
		public static void registerItems(ModelRegistryEvent event) {
			ModBlocks.registerModels();	
		}
    	@SubscribeEvent
    	public static void registerBlocks(RegistryEvent.Register<Block> event) {
    		ModBlocks.register(event.getRegistry());
    	}
    }
}
