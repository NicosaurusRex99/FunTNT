package nicusha.tnt.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, FunTNT.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.BABY_BOOMER, "Baby Boomer TNT");
        addItem(()-> ModBlocks.BABY_BOOMER.asItem(), "Baby Boomer TNT");

        addEntityType(ModEntities.BABY_BOOMER, "Primed Baby Boomer");

        add("itemGroup." + FunTNT.MODID, "Fun TNT");
    }
}