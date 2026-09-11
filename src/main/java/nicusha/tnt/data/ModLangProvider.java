package nicusha.tnt.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.*;
import nicusha.tnt.utils.Utils;

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
        addAuto(ModBlocks.PARTY, "Party TNT");
        addAuto(ModBlocks.THERMAL_FORGE, "Thermal Forge TNT");
        addAuto(ModBlocks.VOLCANO, "Volcano TNT");

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
        addAuto(ModBlocks.PARTY.asItem(), "Party TNT");
        addAuto(ModItems.PARTY_MUSIC_DISC.asItem(), "Party Blast");
        addAuto(ModBlocks.THERMAL_FORGE.asItem(), "Thermal Forge TNT");
        addAuto(ModBlocks.VOLCANO.asItem(), "Volcano TNT");

        addAuto(ModEntities.FERTILIZER.get(), "Primed Fertilizer TNT");
        addAuto(ModEntities.BABY_BOOMER.get(), "Primed Baby Boomer");
        addAuto(ModEntities.NUKE.get(), "If you can read this, its too late");
        addAuto(ModEntities.CRYO.get(), "Primed Cryo TNT");
        addAuto(ModEntities.GRAVITY.get(), "Primed Gravity TNT");
        addAuto(ModEntities.RESTORATION.get(), "Primed Restoration TNT");
        addAuto(ModEntities.LATELY, "Primed Cat Party TNT");
        addAuto(ModEntities.PAINT, "Primed Paint TNT");
        addAuto(ModEntities.PARTY, "Primed Party TNT");
        addAuto(ModEntities.THERMAL_FORGE, "Primed Thermal Forge TNT");
        addAuto(ModEntities.VOLCANO_TNT, "Primed Volcano TNT");

        addAuto("itemGroup." + FunTNT.MODID, "Fun TNT");
        addAuto(FunTNT.MODID + ".too_late", "It is too late now...");
        addAuto("death.attack." + FunTNT.MODID + ".nuke", "%1$s was obliterated by a Nuclear Blast");
        addAuto(FunTNT.MODID + ".sly_withers_lately.reference", "You don't have to stay so cool when you're trying not to break down");
        addAuto("item.fun_tnt.party_music_disc.desc", "Free Sounds Library - Party Blast");
        addAuto("effect.fun_tnt.party_dance", "Party Fever!");
        addAuto(ModEffects.PARTY_DANCE.get(), "Party Fever");
        addAuto(ModSounds.PARTY_BLAST, "Party Blast");
        addAuto("tooltip.fun_tnt.fertilizer.description", "Explodes into a burst of nutrients, instantly growing nearby crops and spreading flora.");
        addAuto("tooltip.fun_tnt.nuke.description", "A catastrophic explosive that leaves a massive crater and flash-evaporates nearby water sources.");
        addAuto("tooltip.fun_tnt.baby_boomer.description", "Converts all mobs into their baby variants.");
        addAuto("tooltip.fun_tnt.cryo.description", "Unleashes a sub-zero shockwave, freezing water blocks into ice and trapping entities in frost.");
        addAuto("tooltip.fun_tnt.gravity.description", "Distorts local physics upon detonation, forcefully pulling or launching all nearby entities.");
        addAuto("tooltip.fun_tnt.trip_mine.description", "A stealthy, stone-disguised explosive device that detonates instantly when stepped off.");
        addAuto("tooltip.fun_tnt.restoration.description", "A specialized anomaly charge that completely reverts explosion and terrain damage in its blast radius.");
        addAuto("tooltip.fun_tnt.lately.description", "You might just have to detonate this one to find out!");
        addAuto("tooltip.fun_tnt.paint.description", "Splashes a burst of colorful pigments across blocks and entities in its blast zone upon detonating.");
        addAuto("tooltip.fun_tnt.party.description", "Triggers a server-authoritative dance rave, spinning nearby survival players and mobs to the beat. Also converts all music discs to the new track!");
        addAuto("tooltip.fun_tnt.thermal_forge.description", "Emits an intense localized heatwave that cleanly smelts ore blocks into refined materials with Fortune I yields.");
        addAuto("tooltip.fun_tnt.volcano.description", "Sets off a volcanic eruption");
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