package com.arsenius_gen.magictp;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MainMod.MOD_ID, value = Dist.CLIENT)
public class ClientChatHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.getMessage().getString();

        // Suppress messages like "[arsenius_gen: Teleported Arsenij_Mine to 0.500000, 0.000000, 0.500000]"
        if (message.matches("\\[.*: Teleported .* to -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]")) {
            event.setCanceled(true); // Prevent the message from being displayed
            MainMod.LOGGER.info("Suppressed teleportation message: " + message);
        }
    }
}