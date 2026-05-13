package nicusha.tnt.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.utils.Utils;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModEntities;
import nicusha.tnt.registry.ModItems;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    protected final String locale;
    private final Map<String, String> existingTranslations = new HashMap<>();
    private static final Gson GSON = new Gson();

    public ModLangProvider(PackOutput output, String locale) {
        super(output, FunTNT.MODID, locale);
        this.locale = locale;
        loadExistingTranslations(output);
    }

    @Override
    protected void addTranslations() {
        addAuto(ModBlocks.BABY_BOOMER, "Baby Boomer TNT");
        addAuto(ModBlocks.NUKE, "NUKE!!!");
        addAuto(ModBlocks.FERTILIZER, "Fertilizer Bomb");
        addAuto(ModBlocks.CRYO, "Cryo TNT");
        addAuto(ModBlocks.GRAVITY, "Gravity TNT");
        addAuto(ModBlocks.TRIP_MINE, "Trip Mine");
        addAuto(ModBlocks.RESTORATION, "Restoration TNT");
        addAuto(ModBlocks.LATELY, "Cat Party TNT");
        addAuto(ModBlocks.PAINT, "Paint TNT");

        addAuto(ModBlocks.BABY_BOOMER.get().asItem(), "Baby Boomer TNT");
        addAuto(ModBlocks.NUKE.get().asItem(), "NUKE!!!");
        addAuto(ModBlocks.FERTILIZER.get().asItem(), "Fertilizer Bomb");
        addAuto(ModItems.DYNAMITE.get(), "Dynamite");
        addAuto(ModBlocks.CRYO.get().asItem(), "Cryo TNT");
        addAuto(ModBlocks.GRAVITY.get().asItem(), "Gravity TNT");
        addAuto(ModBlocks.TRIP_MINE.asItem(), "Trip Mine");
        addAuto(ModBlocks.RESTORATION.asItem(), "Restoration TNT");
        addAuto(ModBlocks.LATELY.asItem(), "Cat Party TNT");
        addAuto(ModBlocks.PAINT.asItem(), "Paint TNT");

        addAuto(ModEntities.FERTILIZER.get(), "Primed Fertilizer TNT");
        addAuto(ModEntities.BABY_BOOMER.get(), "Primed Baby Boomer");
        addAuto(ModEntities.NUKE.get(), "If you can read this, its too late");
        addAuto(ModEntities.CRYO.get(), "Primed Cryo TNT");
        addAuto(ModEntities.GRAVITY.get(), "Primed Gravity TNT");
        addAuto(ModEntities.RESTORATION.get(), "Primed Restoration TNT");
        addAuto(ModEntities.LATELY, "Primed Cat Party TNT");
        addAuto(ModEntities.PAINT, "Primed Paint TNT");

        addAuto("itemGroup." + FunTNT.MODID, "Fun TNT");
        addAuto(FunTNT.MODID + ".too_late", "It is too late now...");
        addAuto("death.attack." + FunTNT.MODID + ".nuke", "%1$s was obliterated by a Nuclear Blast");
        addAuto(FunTNT.MODID + ".sly_withers_lately.reference", "You don't have to stay so cool when you're trying not to break down");
    }

    private void addAuto(Object key, String englishValue) {
        String translationKey = getTranslationKey(key);
        if (translationKey == null) return;
        if (existingTranslations.containsKey(translationKey)) {
            add(translationKey, existingTranslations.get(translationKey));
            return;
        }
        String targetText = englishValue;
        if (!locale.equals("en_us")) {
            String langCode = locale.split("_")[0];
            targetText = Utils.translate(langCode, englishValue);
        }
        add(translationKey, targetText);
    }

    private String getTranslationKey(Object key) {
        if (key instanceof Supplier<?> s) {
            Object val = s.get();
            if (val instanceof Block b) return b.getDescriptionId();
            if (val instanceof Item i) return i.getDescriptionId();
            if (val instanceof EntityType<?> e) return e.getDescriptionId();
        } else {
            if (key instanceof Block b) return b.getDescriptionId();
            if (key instanceof Item i) return i.getDescriptionId();
            if (key instanceof EntityType<?> e) return e.getDescriptionId();
            if (key instanceof String s) return s;
        }
        return null;
    }

    private void loadExistingTranslations(PackOutput output) {
        try {
            Path path = output.getOutputFolder()
                    .resolve("assets")
                    .resolve(FunTNT.MODID)
                    .resolve("lang")
                    .resolve(locale + ".json");

            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path)) {
                    JsonObject json = GSON.fromJson(reader, JsonObject.class);
                    if (json != null) {
                        json.entrySet().forEach(entry ->
                                existingTranslations.put(entry.getKey(), entry.getValue().getAsString())
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("Could not load existing translations for {}: {}", locale, e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Languages: " + FunTNT.MODID + " (" + locale + ")";
    }
}