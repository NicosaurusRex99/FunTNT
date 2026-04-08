package nicusha.tnt.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import nicusha.tnt.FunTNT;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModItems;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, FunTNT.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        TexturedModel babyBoomerModel = TexturedModel.CUBE_TOP_BOTTOM.get(ModBlocks.BABY_BOOMER.get());
        Identifier babyBoomerId = babyBoomerModel.create(ModBlocks.BABY_BOOMER.get(), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.BABY_BOOMER.get(), BlockModelGenerators.plainVariant(babyBoomerId)));
        blockModels.registerSimpleItemModel(ModBlocks.BABY_BOOMER.get(), babyBoomerId);

        TexturedModel nukeModel = TexturedModel.CUBE_TOP_BOTTOM.get(ModBlocks.NUKE.get());
        Identifier nukeId = nukeModel.create(ModBlocks.NUKE.get(), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.NUKE.get(), BlockModelGenerators.plainVariant(nukeId)));
        blockModels.registerSimpleItemModel(ModBlocks.NUKE.get(), nukeId);


        itemModels.generateFlatItem(ModItems.DYNAMITE.get(), ModelTemplates.FLAT_ITEM);
    }
}