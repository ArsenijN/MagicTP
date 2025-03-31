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

        if (message.matches("\\[.*: Teleported .* to -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: Teleported .* to .*]") ||
            message.matches("\\[.*: Телепортовано .* на -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: Телепортовано .* до .*]") || 
            message.matches("\\[.*: .* телепортирован в точку -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: .* телепортирован к .*]")) {
            MagicTP.LOGGER.debug("Message matches teleportation pattern.");
            // Handle the teleportation message
        } else {
            MagicTP.LOGGER.debug("Message does not match any teleportation pattern.");
        }
    }
}