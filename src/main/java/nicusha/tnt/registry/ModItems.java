package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;
import nicusha.tnt.item.DynamiteItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FunTNT.MODID);

    public static final DeferredItem<Item> DYNAMITE =  ITEMS.register("dynamite", () -> new DynamiteItem(new Item.Properties().useCooldown(0.4F).setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(FunTNT.MODID, "dynamite")))));
}