package com.arsenius_gen.magictp;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod("magictp")
public class MainMod {
    public static final String MOD_ID = "magictp";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MainMod() {
        LOGGER.info("MagicTP Mod has loaded!");
        MinecraftForge.EVENT_BUS.register(TeleportEventHandler.class);

        // Register client-side handlers only if running on the client
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(ClientChatHandler.class);
            MinecraftForge.EVENT_BUS.register(ClientMagicMessageHandler.class);
        }
    }
}