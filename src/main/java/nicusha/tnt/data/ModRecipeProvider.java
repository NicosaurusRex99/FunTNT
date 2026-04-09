package nicusha.tnt.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModItems;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.REDSTONE, ModBlocks.BABY_BOOMER.get(), 2).pattern(" P ").pattern("GFG").pattern(" G ").define('P', Items.PAPER).define('G', Items.GUNPOWDER).define('F', ItemTags.SMALL_FLOWERS).unlockedBy("has_gunpowder", this.has(Items.GUNPOWDER)).save(this.output);
        shaped(RecipeCategory.REDSTONE, ModBlocks.NUKE.get()).pattern("FCF").pattern("GTG").pattern("FCF").define('F', Items.TNT).define('C', Items.NETHERITE_BLOCK).define('G', Items.REPEATER).define('T', Items.HEAVY_CORE).unlockedBy("has_tnt", this.has(Items.TNT)).save(this.output);
        shaped(RecipeCategory.COMBAT, ModItems.DYNAMITE.get(), 3).pattern("S").pattern("G").pattern("P").define('S', Items.STRING).define('G', Items.GUNPOWDER).define('P', Items.PAPER).unlockedBy("has_gunpowder", this.has(Items.GUNPOWDER)).save(this.output);
        shaped(RecipeCategory.REDSTONE, ModBlocks.FERTILIZER.get()).pattern("BBB").pattern("BTB").pattern("SSS").define('B', ItemTags.create(Identifier.fromNamespaceAndPath("c", "fertilizers"))).define('T', Items.TNT).define('S', ItemTags.VILLAGER_PLANTABLE_SEEDS).unlockedBy("has_tnt", this.has(Items.TNT)).save(this.output);
        shaped(RecipeCategory.REDSTONE, ModBlocks.CRYO.get()).pattern(" I ").pattern("ITI").pattern(" I ").define('T', Items.TNT).define('I', ItemTags.create(Identifier.fromNamespaceAndPath("c", "ice"))).unlockedBy("has_tnt", this.has(Items.TNT)).save(this.output);
        shaped(RecipeCategory.REDSTONE, ModBlocks.GRAVITY.get()).pattern(" C ").pattern("PTP").pattern(" C ").define('C', Items.COPPER_INGOT).define('P', Blocks.STICKY_PISTON).define('T', Items.TNT).unlockedBy("has_copper", this.has(Items.COPPER_INGOT)).save(this.output);
    }
}