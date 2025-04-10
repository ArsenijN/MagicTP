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
        String teleportRegexClosured = "\\[MC\\]\n.* was moved by magic! To view more, install MagicTP.*";
        String teleportRegexBackwardCompatibility = "^[a-zA-Z0-9_]+\\|-?\\d+(\\.\\d+)?\\|-?\\d+(\\.\\d+)?\\|-?\\d+(\\.\\d+)?$"; // change to "^[a-zA-Z0-9_]{3,16}\|-?\d+(\.\d+)?\|-?\d+(\.\d+)?\|-?\d+(\.\d+)?$" if your server can accept only official types of nicknames (3 to 16 characters)

        // Check if the message matches any teleportation pattern
        if (message.matches(teleportRegex) || message.matches(teleportRegexNamed) || message.matches(teleportRegexBackwardCompatibility) || message.matches(teleportRegexClosured)) {
            MagicTP.LOGGER.debug("Message matches teleportation pattern. Suppressing message.");
            event.setCanceled(true); // Suppress the teleportation message
        } else {
            MagicTP.LOGGER.debug("Message does not match any teleportation pattern. Allowing it to pass through.");
        }
    }
}