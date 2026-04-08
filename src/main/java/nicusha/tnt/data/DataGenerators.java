package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;


public class DataGenerators {

    public static void genData(GatherDataEvent.Client event) {
        event.createProvider(ModLangProvider::new);
        event.createProvider(ModModelProvider::new);
        event.createBlockAndItemTags(ModBlockTagsProvider::new, (output, lookup, blockTags) -> new ModItemTagsProvider(output, lookup));
        event.createProvider((output, lookup) -> ModLootTableProvider.create(output, lookup));

        event.getGenerator().addProvider(true, new ModDamageTypeProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
        event.getGenerator().addProvider(true, new RecipeProvider.Runner(event.getGenerator().getPackOutput(), event.getLookupProvider()) {@Override public String getName() { return "TNT Recipes"; } @Override protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {return new ModRecipeProvider(registries, recipeOutput);}});}
}