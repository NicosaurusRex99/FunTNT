package nicusha.tnt;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue NUKE_RADIUS = BUILDER.comment("Nuke radius").comment("Higher number will mean more lag").defineInRange("nukeRadius", 128, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue BABY_BOOMER_RADIUS = BUILDER.comment("Baby boomer radius").defineInRange("babyBoomerRadius", 5, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue CRYO_RADIUS = BUILDER.comment("Cryo radius").defineInRange("cryoRadius", 6, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FERTILIZER_RADIUS = BUILDER.comment("Fertilizer radius").defineInRange("fertilizerRadius", 10, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue GRAVITY_RADIUS = BUILDER.comment("Gravity radius").defineInRange("gravityRadius", 12, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RESTORATION_RADIUS = BUILDER.comment("Restoration radius").defineInRange("restorationRadius", 8, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue PAINT_RADIUS = BUILDER.comment("Paint radius").defineInRange("paintRadius", 16, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue PARTY_RADIUS = BUILDER.comment("Party radius").defineInRange("partyRadius", 15, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue THERMAL_FORGE_RADIUS = BUILDER.comment("Thermal forge radius").defineInRange("thermalForgeRadius", 16, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue NUKE_FUSE = BUILDER.comment("Nuke fuse").comment("The delay from ignition to explosion").defineInRange("nukeFuse", 300, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue BABY_BOOMER_FUSE = BUILDER.comment("Baby boomer fuse").comment("The delay from ignition to explosion").defineInRange("babyBoomerFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue CRYO_FUSE = BUILDER.comment("Cryo fuse").comment("The delay from ignition to explosion").defineInRange("cryoFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FERTILIZER_FUSE = BUILDER.comment("Fertilizer fuse").comment("The delay from ignition to explosion").defineInRange("fertilizerFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue GRAVITY_FUSE = BUILDER.comment("Gravity fuse").comment("The delay from ignition to explosion").defineInRange("gravityFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RESTORATION_FUSE = BUILDER.comment("Restoration fuse").comment("The delay from ignition to explosion").defineInRange("restorationFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue PAINT_FUSE = BUILDER.comment("Paint fuse").comment("The delay from ignition to explosion").defineInRange("paintFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue PARTY_FUSE = BUILDER.comment("Party fuse").comment("The delay from ignition to explosion").defineInRange("partyFuse", 40, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue THERMAL_FORGE_FUSE = BUILDER.comment("Thermal forge fuse").comment("The delay from ignition to explosion").defineInRange("thermalForgeFuse", 60, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue VOLCANO_HEIGHT = BUILDER.comment("Volcano target height in blocks/layers").defineInRange("volcanoHeight", 14, 3, 64);
    public static final ModConfigSpec.IntValue VOLCANO_BASE_RADIUS = BUILDER.comment("Volcano base radius in blocks").comment("Higher values create a wider base footprint").defineInRange("volcanoBaseRadius", 38, 4, 128);
    public static final ModConfigSpec.IntValue VOLCANO_VENTING_TICKS = BUILDER.comment("Smoke venting phase duration in ticks before cone construction").defineInRange("volcanoVentingTicks", 80, 10, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue VOLCANO_ERUPTION_TICKS = BUILDER.comment("Eruption phase duration in ticks launching magma bombs").defineInRange("volcanoEruptionTicks", 240, 20, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue VOLCANO_TICKS_PER_LAYER = BUILDER.comment("Delay in ticks per layer placement during cone construction").defineInRange("volcanoTicksPerLayer", 5, 1, 100);

    static final ModConfigSpec SPEC = BUILDER.build();
}