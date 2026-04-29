package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;


public class DataGenerators {

    public static void genData(GatherDataEvent.Client event) {
        event.getGenerator().addProvider(true, new ModLangProvider(event.getGenerator().getPackOutput(), "en_us"));
        String[] popularLanguages = {"es_es", "fr_fr", "de_de", "zh_cn", "ja_jp", "ru_ru", "hi_in", "ar_sa", "th_th", "tk_ph", "sv_se", "pt_pt", "it_it"};
        for (String lang : popularLanguages) {
            event.getGenerator().addProvider(true, new ModLangProvider(event.getGenerator().getPackOutput(), lang));
        }

        event.createProvider(ModModelProvider::new);
        event.createBlockAndItemTags(ModBlockTagsProvider::new, (output, lookup, blockTags) -> new ModItemTagsProvider(output, lookup));
        event.createProvider((output, lookup) -> ModLootTableProvider.create(output, lookup));

        event.getGenerator().addProvider(true, new ModDamageTypeProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(true, new RecipeProvider.Runner(event.getGenerator().getPackOutput(), event.getLookupProvider()) {@Override public String getName() { return "TNT Recipes"; } @Override protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {return new ModRecipeProvider(registries, recipeOutput);}});}
}