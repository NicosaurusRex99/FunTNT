package nicusha.tnt.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.Utils;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    protected final String locale;

    public ModLangProvider(PackOutput output, String locale) {
        super(output, FunTNT.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        addAuto(ModBlocks.BABY_BOOMER, "Baby Boomer TNT");
        addAuto(ModBlocks.NUKE, "NUKE!!!");
        addAuto(ModBlocks.FERTILIZER, "Fertilizer Bomb");
        addAuto(ModBlocks.CRYO, "Cryo TNT");
        addAuto(ModBlocks.GRAVITY, "Gravity TNT");

        addAuto(ModBlocks.BABY_BOOMER.get().asItem(), "Baby Boomer TNT");
        addAuto(ModBlocks.NUKE.get().asItem(), "NUKE!!!");
        addAuto(ModBlocks.FERTILIZER.get().asItem(), "Fertilizer Bomb");
        addAuto(ModItems.DYNAMITE.get(), "Dynamite");
        addAuto(ModBlocks.CRYO.get().asItem(), "Cryo TNT");
        addAuto(ModBlocks.GRAVITY.get().asItem(), "Gravity TNT");

        addAuto(ModEntities.FERTILIZER.get(), "Primed Fertilizer TNT");
        addAuto(ModEntities.BABY_BOOMER.get(), "Primed Baby Boomer");
        addAuto(ModEntities.NUKE.get(), "If you can read this, its too late");
        addAuto(ModEntities.CRYO.get(), "Primed Cryo TNT");
        addAuto(ModEntities.GRAVITY.get(), "Primed Gravity TNT");

        addAuto("itemGroup." + FunTNT.MODID, "Fun TNT");
        addAuto(FunTNT.MODID + ".too_late", "It is too late now...");
        addAuto("death.attack." + FunTNT.MODID + ".nuke", "%1$s was obliterated by a Nuclear Blast");
    }

    private void addAuto(Object key, String englishValue) {
        String targetText = englishValue;

        if (!locale.equals("en_us")) {
            String langCode = locale.split("_")[0];
            targetText = Utils.translate(langCode, englishValue);
        }

        if (key instanceof Supplier<?> s) {
            Object val = s.get();
            if (val instanceof Block b) add(b, targetText);
            else if (val instanceof Item i) add(i, targetText);
            else if (val instanceof EntityType<?> e) add(e, targetText);
        } else {
            if (key instanceof Block b) add(b, targetText);
            else if (key instanceof Item i) add(i, targetText);
            else if (key instanceof EntityType<?> e) add(e, targetText);
            else if (key instanceof String s) add(s, targetText);
        }
    }
}