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

        // Load regex patterns dynamically based on the current locale
        String teleportRegex = LocaleRegexLoader.getRegex("teleport_regex");
        String teleportRegexNamed = LocaleRegexLoader.getRegex("teleport_regex_named");

        // Check if the message matches any teleportation pattern
        if (message.matches(teleportRegex) || message.matches(teleportRegexNamed)) {
            MagicTP.LOGGER.debug("Message matches teleportation pattern. Suppressing message.");
            event.setCanceled(true); // Suppress the teleportation message
        } else {
            MagicTP.LOGGER.debug("Message does not match any teleportation pattern. Allowing it to pass through.");
        }
    }
}