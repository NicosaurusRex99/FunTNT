package nicusha.tnt.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import nicusha.tnt.FunTNT;
import nicusha.tnt.blocks.PaintTntBlock;
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
        registerTnt(ModBlocks.LATELY.get(), blockModels);
        registerTnt(ModBlocks.PARTY.get(), blockModels);
        registerTnt(ModBlocks.THERMAL_FORGE.get(), blockModels);
        registerVolcanoTnt(ModBlocks.VOLCANO.get(), blockModels);
        registerPaintTnt(ModBlocks.PAINT.get(), blockModels);
        itemModels.generateFlatItem(ModItems.DYNAMITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PARTY_MUSIC_DISC.get(), ModelTemplates.MUSIC_DISC);
        registerTripMine(ModBlocks.TRIP_MINE.get(), blockModels);
    }


    private void registerTnt(Block block, BlockModelGenerators generators) {
        TexturedModel model = TexturedModel.CUBE_TOP_BOTTOM.get(block);
        Identifier modelId = model.create(block, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        generators.registerSimpleItemModel(block, modelId);
    }

    private void registerVolcanoTnt(Block block, BlockModelGenerators generators) {
        Identifier modelId = ModelLocationUtils.getModelLocation(block);
        generators.modelOutput.accept(modelId, () -> {
            JsonObject json = new JsonObject();
            JsonObject texObj = new JsonObject();
            texObj.addProperty("0", "minecraft:block/tnt_side");
            texObj.addProperty("1", "minecraft:block/tnt_bottom");
            texObj.addProperty("2", "minecraft:block/basalt_side");
            texObj.addProperty("3", "minecraft:block/basalt_top");
            texObj.addProperty("4", "minecraft:block/magma");
            texObj.addProperty("5", "minecraft:block/lava_still");
            texObj.addProperty("particle", "minecraft:block/tnt_side");
            json.add("textures", texObj);
            json.add("elements", createVolcanoElements());
            JsonObject display = new JsonObject();
            JsonObject gui = new JsonObject();
            gui.add("rotation", Stream.of(30, 225, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            gui.add("translation", Stream.of(0, -1, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            gui.add("scale", Stream.of(0.55, 0.55, 0.55).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            display.add("gui", gui);
            JsonObject thirdperson = new JsonObject();
            thirdperson.add("rotation", Stream.of(75, 45, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            thirdperson.add("translation", Stream.of(0, 2.5, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            thirdperson.add("scale", Stream.of(0.375, 0.375, 0.375).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            display.add("thirdperson_righthand", thirdperson);
            JsonObject firstperson = new JsonObject();
            firstperson.add("rotation", Stream.of(0, 45, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            firstperson.add("scale", Stream.of(0.4, 0.4, 0.4).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
            display.add("firstperson_righthand", firstperson);
            json.add("display", display);
            return json;
        });
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        generators.registerSimpleItemModel(block, modelId);
    }

    private JsonArray createVolcanoElements() {
        JsonArray elements = new JsonArray();
        JsonObject tntBase = new JsonObject();
        tntBase.addProperty("name", "tnt_base");
        tntBase.add("from", Stream.of(0, 0, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        tntBase.add("to", Stream.of(16, 8, 16).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject baseFaces = new JsonObject();
        baseFaces.add("north", createFace(new double[]{0, 8, 16, 16}, "#0"));
        baseFaces.add("east",  createFace(new double[]{0, 8, 16, 16}, "#0"));
        baseFaces.add("south", createFace(new double[]{0, 8, 16, 16}, "#0"));
        baseFaces.add("west",  createFace(new double[]{0, 8, 16, 16}, "#0"));
        baseFaces.add("up",    createFace(new double[]{0, 0, 16, 16}, "#4"));
        baseFaces.add("down",  createFace(new double[]{0, 0, 16, 16}, "#1"));
        tntBase.add("faces", baseFaces);
        elements.add(tntBase);
        JsonObject basaltSkirt = new JsonObject();
        basaltSkirt.addProperty("name", "basalt_skirt");
        basaltSkirt.add("from", Stream.of(-0.5, 0, -0.5).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        basaltSkirt.add("to", Stream.of(16.5, 4, 16.5).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject skirtFaces = new JsonObject();
        skirtFaces.add("north", createFace(new double[]{0, 12, 16, 16}, "#2"));
        skirtFaces.add("east",  createFace(new double[]{0, 12, 16, 16}, "#2"));
        skirtFaces.add("south", createFace(new double[]{0, 12, 16, 16}, "#2"));
        skirtFaces.add("west",  createFace(new double[]{0, 12, 16, 16}, "#2"));
        skirtFaces.add("up",    createFace(new double[]{0, 0, 16, 16}, "#3"));
        basaltSkirt.add("faces", skirtFaces);
        elements.add(basaltSkirt);
        JsonObject midCone = new JsonObject();
        midCone.addProperty("name", "volcano_mid_cone");
        midCone.add("from", Stream.of(1, 8, 1).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        midCone.add("to", Stream.of(15, 12, 15).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject midFaces = new JsonObject();
        midFaces.add("north", createFace(new double[]{1, 4, 15, 8}, "#2"));
        midFaces.add("east",  createFace(new double[]{1, 4, 15, 8}, "#2"));
        midFaces.add("south", createFace(new double[]{1, 4, 15, 8}, "#2"));
        midFaces.add("west",  createFace(new double[]{1, 4, 15, 8}, "#2"));
        midFaces.add("up",    createFace(new double[]{1, 1, 15, 15}, "#3"));
        midCone.add("faces", midFaces);
        elements.add(midCone);
        JsonObject topPeak = new JsonObject();
        topPeak.addProperty("name", "volcano_top_peak");
        topPeak.add("from", Stream.of(3, 12, 3).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        topPeak.add("to", Stream.of(13, 15, 13).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject topFaces = new JsonObject();
        topFaces.add("north", createFace(new double[]{3, 1, 13, 4}, "#2"));
        topFaces.add("east",  createFace(new double[]{3, 1, 13, 4}, "#2"));
        topFaces.add("south", createFace(new double[]{3, 1, 13, 4}, "#2"));
        topFaces.add("west",  createFace(new double[]{3, 1, 13, 4}, "#2"));
        topFaces.add("up",    createFace(new double[]{3, 3, 13, 13}, "#3"));
        topPeak.add("faces", topFaces);
        elements.add(topPeak);
        JsonObject craterRim = new JsonObject();
        craterRim.addProperty("name", "crater_rim");
        craterRim.add("from", Stream.of(4, 15, 4).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        craterRim.add("to", Stream.of(12, 16.5, 12).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject rimFaces = new JsonObject();
        rimFaces.add("north", createFace(new double[]{4, 0, 12, 2}, "#2"));
        rimFaces.add("east",  createFace(new double[]{4, 0, 12, 2}, "#2"));
        rimFaces.add("south", createFace(new double[]{4, 0, 12, 2}, "#2"));
        rimFaces.add("west",  createFace(new double[]{4, 0, 12, 2}, "#2"));
        rimFaces.add("up",    createFace(new double[]{4, 4, 12, 12}, "#3"));
        craterRim.add("faces", rimFaces);
        elements.add(craterRim);
        JsonObject lavaCore = new JsonObject();
        lavaCore.addProperty("name", "lava_core");
        lavaCore.add("from", Stream.of(5, 12, 5).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        lavaCore.add("to", Stream.of(11, 17, 11).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject lavaFaces = new JsonObject();
        lavaFaces.add("north", createFace(new double[]{5, 0, 11, 5}, "#4"));
        lavaFaces.add("east",  createFace(new double[]{5, 0, 11, 5}, "#4"));
        lavaFaces.add("south", createFace(new double[]{5, 0, 11, 5}, "#4"));
        lavaFaces.add("west",  createFace(new double[]{5, 0, 11, 5}, "#4"));
        lavaFaces.add("up",    createFace(new double[]{5, 5, 11, 11}, "#5"));
        lavaCore.add("faces", lavaFaces);
        elements.add(lavaCore);
        JsonObject magmaPeak = new JsonObject();
        magmaPeak.addProperty("name", "bubbling_magma_peak");
        magmaPeak.add("from", Stream.of(7, 17, 7).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        magmaPeak.add("to", Stream.of(9, 18.5, 9).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        JsonObject magmaFaces = new JsonObject();
        magmaFaces.add("north", createFace(new double[]{7, 0, 9, 2}, "#5"));
        magmaFaces.add("east",  createFace(new double[]{7, 0, 9, 2}, "#5"));
        magmaFaces.add("south", createFace(new double[]{7, 0, 9, 2}, "#5"));
        magmaFaces.add("west",  createFace(new double[]{7, 0, 9, 2}, "#5"));
        magmaFaces.add("up",    createFace(new double[]{7, 7, 9, 9}, "#5"));
        magmaPeak.add("faces", magmaFaces);
        elements.add(magmaPeak);
        return elements;
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

    private void registerPaintTnt(Block block, BlockModelGenerators generators) {
        var propertyDispatch = PropertyDispatch.initial(PaintTntBlock.COLOR);
        Identifier itemModelId = null;
        for (DyeColor color : DyeColor.values()) {
            Identifier colorModelId = ModelLocationUtils.getModelLocation(block).withSuffix("_" + color.getName());
            if (color == DyeColor.WHITE) {
                itemModelId = colorModelId;
            }
            generators.modelOutput.accept(colorModelId, () -> {
                JsonObject json = new JsonObject();
                json.addProperty("parent", "minecraft:block/cube_bottom_top");
                JsonObject texObj = new JsonObject();
                texObj.addProperty("top", "minecraft:block/tnt_top");
                texObj.addProperty("bottom", "minecraft:block/tnt_bottom");
                texObj.addProperty("side", "minecraft:block/tnt_side");
                texObj.addProperty("particle", "minecraft:block/tnt_side");
                json.add("textures", texObj);
                JsonArray elements = new JsonArray();
                JsonObject cube = new JsonObject();
                cube.add("from", Stream.of(0, 0, 0).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
                cube.add("to", Stream.of(16, 16, 16).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
                JsonObject faces = new JsonObject();
                int tintIdx = 0;
                faces.add("down",  createTintedFace(new double[]{0, 0, 16, 16}, "#bottom", "down", tintIdx));
                faces.add("up",    createTintedFace(new double[]{0, 0, 16, 16}, "#top", "up", tintIdx));
                faces.add("north", createTintedFace(new double[]{0, 0, 16, 16}, "#side", "north", tintIdx));
                faces.add("south", createTintedFace(new double[]{0, 0, 16, 16}, "#side", "south", tintIdx));
                faces.add("west",  createTintedFace(new double[]{0, 0, 16, 16}, "#side", "west", tintIdx));
                faces.add("east",  createTintedFace(new double[]{0, 0, 16, 16}, "#side", "east", tintIdx));
                cube.add("faces", faces);
                elements.add(cube);
                json.add("elements", elements);
                return json;
            });
            propertyDispatch.select(color, BlockModelGenerators.plainVariant(colorModelId));
        }
        generators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(propertyDispatch));
        if (itemModelId != null) {
            generators.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block).withSuffix("_" + DyeColor.RED.getName()));
        }
    }

    private JsonObject createFace(double[] uv, String texture) {
        JsonObject face = new JsonObject();
        JsonArray uvArray = new JsonArray();
        for (double d : uv) uvArray.add(d);
        face.add("uv", uvArray);
        face.addProperty("texture", texture);
        return face;
    }

    private JsonObject createTintedFace(double[] uv, String texture, String cullface, int tintIndex) {
        JsonObject face = createFace(uv, texture);
        face.addProperty("cullface", cullface);
        face.addProperty("tintindex", tintIndex);
        return face;
    }
}