package nicusha.tnt.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nicusha.tnt.FunTNT;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FunTNT.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FUN_TNT_TAB = TABS.register("fun_tnt_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + FunTNT.MODID)).icon(() -> ModBlocks.BABY_BOOMER.get().asItem().getDefaultInstance()).build());
}