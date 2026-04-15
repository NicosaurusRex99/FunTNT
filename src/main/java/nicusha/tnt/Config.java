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

    public static final ModConfigSpec.IntValue NUKE_FUSE = BUILDER.comment("Nuke fuse").comment("The delay from ignition to explosion").defineInRange("nukeFuse", 300, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue BABY_BOOMER_FUSE = BUILDER.comment("Baby boomer fuse").comment("The delay from ignition to explosion").defineInRange("babyBoomerFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue CRYO_FUSE = BUILDER.comment("Cryo fuse").comment("The delay from ignition to explosion").defineInRange("cryoFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue FERTILIZER_FUSE = BUILDER.comment("Fertilizer fuse").comment("The delay from ignition to explosion").defineInRange("fertilizerFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue GRAVITY_FUSE = BUILDER.comment("Gravity fuse").comment("The delay from ignition to explosion").defineInRange("gravityFuse", 80, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue RESTORATION_FUSE = BUILDER.comment("Restoration fuse").comment("The delay from ignition to explosion").defineInRange("restorationFuse", 80, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

}