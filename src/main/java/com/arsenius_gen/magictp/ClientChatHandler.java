package com.arsenius_gen.magictp;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MagicTP.MOD_ID, value = Dist.CLIENT)
public class ClientChatHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.getMessage().getString();
        MagicTP.LOGGER.debug("Received chat message: " + message);

        // Process only teleportation-related messages
        if (message.matches("Teleported .* to -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+")) {
            MagicTP.LOGGER.debug("Message matches teleportation pattern.");
            // Handle the teleportation message
        } else {
            MagicTP.LOGGER.debug("Message does not match any teleportation pattern. Allowing it to pass through.");
        }
    }
}