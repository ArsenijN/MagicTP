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
        // New options:
        public final ForgeConfigSpec.BooleanValue announceUnsupportedLocaleOnJoin;
        public final ForgeConfigSpec.BooleanValue announceUnsupportedLocaleMagic;
        public final ForgeConfigSpec.DoubleValue teleportThreshold;
        public final ForgeConfigSpec.DoubleValue loggingThreshold;
        public final ForgeConfigSpec.BooleanValue magicMessageGlobal;
        public final ForgeConfigSpec.BooleanValue showExtendedConfigurables;
        public final ForgeConfigSpec.BooleanValue playerDisclosure;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("MagicTP Settings");
        
            showExtendedConfigurables = builder
                .comment("Show extended (server-side) configurables in the configuration screen")
                .define("showExtendedConfigurables", false);
            
            playerDisclosure = builder
                .comment("Message shown when player coordinates are hidden")
                .define("playerDisclosure", true); //"Player was moved by magic, but coordinates are hidden.");
        
            announceUnsupportedLocaleOnJoin = builder
                .comment("Announce unsupported locale in chat when the player joins or switches locale")
                .define("announceUnsupportedLocaleOnJoin", true);
            announceUnsupportedLocaleMagic = builder
                .comment("Announce unsupported locale in chat during magic message fallback")
                .define("announceUnsupportedLocaleMagic", true);
        
            teleportThreshold = builder
                .comment("Teleport threshold value")
                .defineInRange("teleportThreshold", 10.0, 0.0, Double.MAX_VALUE);
            loggingThreshold = builder
                .comment("Logging threshold value")
                .defineInRange("loggingThreshold", 10.0, 0.0, Double.MAX_VALUE);
            magicMessageGlobal = builder
                .comment("Should magic teleportation messages be global (visible to all players)?")
                .define("magicMessageGlobal", false);
        
            builder.pop();
        }
    }
}