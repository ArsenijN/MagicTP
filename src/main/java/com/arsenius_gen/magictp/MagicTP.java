package com.arsenius_gen.magictp;

import com.arsenius_gen.magictp.client.MagicTPConfigScreen;
import com.arsenius_gen.magictp.commands.MagicTPCommand;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod("magictp")
public class MagicTP {
    public static final String MOD_ID = "magictp";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEBUG;

    static {
        String debugProperty = System.getProperty("debug", "false");
        LOGGER.info("Debug property value: " + debugProperty);
        DEBUG = Boolean.parseBoolean(debugProperty);
        LOGGER.info("DEBUG flag is set to: " + DEBUG);
    }

    public MagicTP() {
        LOGGER.info("MagicTP Mod has loaded!");
        if (DEBUG) {
            LOGGER.info("Debug mode is enabled!");
        }

        // Register server-side handlers
        MinecraftForge.EVENT_BUS.register(TeleportEventHandler.class);

        // Register client-side handlers only if running on the client
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(ClientChatHandler.class);
            MinecraftForge.EVENT_BUS.register(ClientMagicMessageHandler.class);

            // Register the config screen
            MagicTPConfigScreen.register();
        }

        // Register the configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MagicTPConfig.COMMON_SPEC);

        // Register commands
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        MagicTPCommand.register(event.getDispatcher());
    }
}