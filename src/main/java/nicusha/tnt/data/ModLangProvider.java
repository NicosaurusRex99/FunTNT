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
        addBlock(ModBlocks.NUKE, "NUKE!!!");
        addItem(()-> ModBlocks.NUKE.asItem(), "NUKE");

        addEntityType(ModEntities.BABY_BOOMER, "Primed Baby Boomer");
        addEntityType(ModEntities.NUKE, "If you can read this, its too late");

        add("itemGroup." + FunTNT.MODID, "Fun TNT");
        add(FunTNT.MODID + ".oppenheimer", "I am become Death, the destroyer of worlds.");
        add("death.attack.funtnt.nuke", "%1$s was obliterated by a Nuclear Blast");
    }
}