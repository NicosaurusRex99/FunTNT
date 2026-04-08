package nicusha.tnt.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, FunTNT.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.BABY_BOOMER, "Baby Boomer TNT");
        addItem(()-> ModBlocks.BABY_BOOMER.asItem(), "Baby Boomer TNT");
        addItem(()-> ModBlocks.NUKE.asItem(), "NUKE!!!");
        addItem(()-> ModItems.DYNAMITE.get(), "Dynamite");


        addBlock(ModBlocks.NUKE, "NUKE!!!");

        addEntityType(ModEntities.BABY_BOOMER, "Primed Baby Boomer");
        addEntityType(ModEntities.NUKE, "If you can read this, its too late");

        add("itemGroup." + FunTNT.MODID, "Fun TNT");
        add(FunTNT.MODID + ".too_late", "It is too late now...");
        add("death.attack.funtnt.nuke", "%1$s was obliterated by a Nuclear Blast");
    }
}