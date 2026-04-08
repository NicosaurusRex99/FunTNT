package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import nicusha.tnt.registry.ModBlocks;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.REDSTONE, ModBlocks.BABY_BOOMER.get(), 2).pattern(" P ").pattern("GFG").pattern(" G ").define('P', Items.PAPER).define('G', Items.GUNPOWDER).define('F', ItemTags.SMALL_FLOWERS).unlockedBy("has_gunpowder", this.has(Items.GUNPOWDER)).save(this.output);
        this.shaped(RecipeCategory.REDSTONE, ModBlocks.NUKE.get()).pattern("FCF").pattern("GTG").pattern("FCF").define('F', Items.TNT).define('C', Items.NETHERITE_BLOCK).define('G', Items.REPEATER).define('T', Items.HEAVY_CORE).unlockedBy("has_tnt", this.has(Items.TNT)).save(this.output);
    }
}