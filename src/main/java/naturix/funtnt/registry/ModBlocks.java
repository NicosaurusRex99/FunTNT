package naturix.funtnt.registry;

import naturix.funtnt.blocks.TNTBase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	public static TNTBase tnt2 = new TNTBase("tnt_2", 2f);
	public static TNTBase tnt8 = new TNTBase("tnt_8", 8f);
	public static TNTBase tnt64 = new TNTBase("tnt_64", 64f);
	public static TNTBase tnt128 = new TNTBase("tnt_128", 128f);
	public static TNTBase nuke = new TNTBase("tnt_nuke", 256f);
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				tnt2,
				tnt8,
				tnt64,
				tnt128,
				nuke
				);
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				tnt2.createItemBlock(),
				tnt8.createItemBlock(),
				tnt64.createItemBlock(),
				tnt128.createItemBlock(),
				nuke.createItemBlock()
				);
	}

	public static void registerModels() {
		tnt2.registerItemModel(Item.getItemFromBlock(tnt2));
		tnt8.registerItemModel(Item.getItemFromBlock(tnt8));
		tnt64.registerItemModel(Item.getItemFromBlock(tnt64));
		tnt128.registerItemModel(Item.getItemFromBlock(tnt128));
		nuke.registerItemModel(Item.getItemFromBlock(nuke));
	}

}