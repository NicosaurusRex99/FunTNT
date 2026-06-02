package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;
import nicusha.tnt.blocks.BaseCustomTntBlock;
import nicusha.tnt.blocks.PaintTntBlock;
import nicusha.tnt.blocks.TripMineBlock;
import nicusha.tnt.entities.*;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FunTNT.MODID);

    public static final DeferredBlock<Block> FERTILIZER = registerTnt("fertilizer", FertilizerEntity::new, 80);
    public static final DeferredBlock<Block> NUKE = registerTnt("nuke", NukeEntity::new, 200);
    public static final DeferredBlock<Block> BABY_BOOMER = registerTnt("baby_boomer", BabyBoomerEntity::new, 40);
    public static final DeferredBlock<Block> CRYO = registerTnt("cryo", CryoTntEntity::new, 40);
    public static final DeferredBlock<Block> GRAVITY = registerTnt("gravity", GravityTntEntity::new, 100);
    public static final DeferredBlock<TripMineBlock> TRIP_MINE = registerBlock("trip_mine", () -> new TripMineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(0.5f).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(FunTNT.MODID, "trip_mine")))));
    public static final DeferredBlock<Block> RESTORATION = registerTnt("restoration", RestorationTntEntity::new, 100);
    public static final DeferredBlock<Block> LATELY = registerTnt("lately", LatelyEntity::new, 10);
    public static final DeferredBlock<Block> PAINT = registerBlock("paint", () -> new PaintTntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(FunTNT.MODID, "paint"))), PaintTntEntity::new, 100));
    public static final DeferredBlock<Block> PARTY = registerTnt("party", PartyTntEntity::new, 40);

    private static DeferredBlock<Block> registerTnt(String name, BaseCustomTntBlock.TntFactory<?> factory, int fuse) {
        return registerBlock(name, () -> new BaseCustomTntBlock(Block.Properties.ofFullCopy(Blocks.TNT).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(FunTNT.MODID, name))), factory, fuse));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> {
            Item.Properties itemProps = new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FunTNT.MODID, name)));
            return new BlockItem(block.get(), itemProps);
        });

        return block;
    }
}