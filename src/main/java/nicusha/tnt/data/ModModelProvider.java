package nicusha.tnt.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModItems;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, FunTNT.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerTnt(ModBlocks.BABY_BOOMER.get(), blockModels);
        registerTnt(ModBlocks.NUKE.get(), blockModels);
        registerTnt(ModBlocks.FERTILIZER.get(), blockModels);

        itemModels.generateFlatItem(ModItems.DYNAMITE.get(), ModelTemplates.FLAT_ITEM);
    }


    private void registerTnt(Block block, BlockModelGenerators generators) {
        TexturedModel model = TexturedModel.CUBE_TOP_BOTTOM.get(block);
        Identifier modelId = model.create(block, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        generators.registerSimpleItemModel(block, modelId);
    }
}