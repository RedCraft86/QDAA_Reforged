package com.redcraft86.qdaa;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientCfg {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.EnumValue<ScaleFactor> SCALE_FACTOR = BUILDER
            .comment("Scale Factor for the super-sampled resolution.")
            .comment("OFF:    1.0x")
            .comment("LOW:    1.25x")
            .comment("MEDIUM: 1.5x")
            .comment("HIGH:   1.75x")
            .comment("ULTRA:  2.0x")
            .defineEnum("scaleFactor", ScaleFactor.MEDIUM);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}