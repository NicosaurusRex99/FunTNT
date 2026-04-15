package nicusha.tnt.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import nicusha.tnt.FunTNT;
import nicusha.tnt.blocks.TripMineBlock;
import nicusha.tnt.registry.ModBlocks;
import nicusha.tnt.registry.ModItems;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, FunTNT.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerTnt(ModBlocks.BABY_BOOMER.get(), blockModels);
        registerTnt(ModBlocks.NUKE.get(), blockModels);
        registerTnt(ModBlocks.FERTILIZER.get(), blockModels);
        registerTnt(ModBlocks.CRYO.get(), blockModels);
        registerTnt(ModBlocks.GRAVITY.get(), blockModels);
        registerTnt(ModBlocks.RESTORATION.get(), blockModels);
        itemModels.generateFlatItem(ModItems.DYNAMITE.get(), ModelTemplates.FLAT_ITEM);
        registerTripMine(ModBlocks.TRIP_MINE.get(), blockModels);
    }


    private void registerTnt(Block block, BlockModelGenerators generators) {
        TexturedModel model = TexturedModel.CUBE_TOP_BOTTOM.get(block);
        Identifier modelId = model.create(block, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        generators.registerSimpleItemModel(block, modelId);
    }

    private void registerTripMine(Block block, BlockModelGenerators generators) {
        Identifier unpressedModel = ModelLocationUtils.getModelLocation(block);
        generators.modelOutput.accept(unpressedModel, () -> {
            JsonObject json = new JsonObject();
            JsonObject texObj = new JsonObject();
            texObj.addProperty("0", "minecraft:block/stone");
            texObj.addProperty("1", "minecraft:block/target_side");
            texObj.addProperty("particle", "minecraft:block/stone");
            json.add("textures", texObj);
            json.add("elements", createTripMineElements(1.0, 2.0));
            return json;
        });
        Identifier pressedModel = ModelLocationUtils.getModelLocation(block, "_pressed");
        generators.modelOutput.accept(pressedModel, () -> {
            JsonObject json = new JsonObject();
            JsonObject texObj = new JsonObject();
            texObj.addProperty("0", "minecraft:block/stone");
            texObj.addProperty("1", "minecraft:block/target_side");
            texObj.addProperty("particle", "minecraft:block/stone");
            json.add("textures", texObj);
            json.add("elements", createTripMineElements(0.01, 1.01));
            return json;
        });
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(TripMineBlock.PRESSED).select(false, BlockModelGenerators.plainVariant(unpressedModel)).select(true, BlockModelGenerators.plainVariant(pressedModel))));
        generators.registerSimpleItemModel(block, unpressedModel);
    }

    private JsonArray createTripMineElements(double padMinY, double padMaxY) {
        JsonArray elements = new JsonArray();
        JsonObject base = new JsonObject();
        base.addProperty("name", "Base");
        base.add("from", Stream.of(1, 0, 1).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        base.add("to", Stream.of(15, 1, 15).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject baseFaces = new JsonObject();
        baseFaces.add("north", createFace(new double[]{1, 14, 15, 15}, "#0"));
        baseFaces.add("east",  createFace(new double[]{1, 1, 15, 2}, "#0"));
        baseFaces.add("south", createFace(new double[]{1, 14, 15, 15}, "#0"));
        baseFaces.add("west",  createFace(new double[]{1, 1, 15, 2}, "#0"));
        baseFaces.add("up",    createFace(new double[]{1, 1, 15, 15}, "#0"));
        baseFaces.add("down",  createFace(new double[]{1, 1, 15, 15}, "#0"));
        base.add("faces", baseFaces);
        elements.add(base);
        JsonObject pad = new JsonObject();
        pad.addProperty("name", "PressurePad");
        pad.add("from", Stream.of(5.0, padMinY, 5.0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        pad.add("to", Stream.of(11.0, padMaxY, 11.0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject padFaces = new JsonObject();
        padFaces.add("north", createFace(new double[]{4, 11, 12, 12}, "#1"));
        padFaces.add("east",  createFace(new double[]{4, 4, 12, 5}, "#1"));
        padFaces.add("south", createFace(new double[]{4, 11, 12, 12}, "#1"));
        padFaces.add("west",  createFace(new double[]{4, 4, 12, 5}, "#1"));
        padFaces.add("up",    createFace(new double[]{4, 4, 12, 12}, "#1"));
        padFaces.add("down",  createFace(new double[]{4, 4, 12, 12}, "#1"));
        pad.add("faces", padFaces);
        elements.add(pad);
        return elements;
    }

    private JsonObject createFace(double[] uv, String texture) {
        JsonObject face = new JsonObject();
        JsonArray uvArray = new JsonArray();
        for (double d : uv) uvArray.add(d);
        face.add("uv", uvArray);
        face.addProperty("texture", texture);
        return face;
    }
}