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

        if (MainMod.DEBUG) {
            MainMod.LOGGER.info("Received chat message: " + message);
        }

        if (message.matches("\\[.*: Teleported .* to -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: Teleported .* to .*]") ||
            message.matches("\\[.*: Телепортовано .* на -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: Телепортовано .* до .*]") || 
            message.matches("\\[.*: .* телепортирован в точку -?\\d+\\.\\d+, -?\\d+\\.\\d+, -?\\d+\\.\\d+\\]") || 
            message.matches("\\[.*: .* телепортирован к .*]")) {
            event.setCanceled(true);
            MainMod.LOGGER.info("Suppressed teleportation message: " + message);
        } else if (MainMod.DEBUG) {
            MainMod.LOGGER.info("Message did not match suppression patterns: " + message);
        }
    }
}