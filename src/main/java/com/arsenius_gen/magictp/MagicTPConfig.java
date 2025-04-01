package com.arsenius_gen.magictp;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MagicTPConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {
        public final ForgeConfigSpec.DoubleValue teleportThreshold;
        public final ForgeConfigSpec.DoubleValue loggingThreshold;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("MagicTP Settings");

            teleportThreshold = builder
                .comment("The distance threshold to detect teleportation (default: 5.0)")
                .defineInRange("teleportThreshold", 5.0, 1.0, 100.0);

            loggingThreshold = builder
                .comment("The distance threshold for logging player movement (default: 1.0)")
                .defineInRange("loggingThreshold", 1.0, 0.1, 10.0);

            builder.pop();
        }
    }
}