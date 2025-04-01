package com.arsenius_gen.magictp;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "magictp", value = Dist.CLIENT)
public class ClientMagicMessageHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        String rawMessage = event.getMessage().getString();
        MagicTP.LOGGER.debug("Received chat message: " + rawMessage);

        // Check if the message is a MagicTP-specific message
        if (rawMessage.contains("|") && rawMessage.split("\\|").length == 4) {
            try {
                String[] parts = rawMessage.split("\\|");
                String playerName = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);

                // Format the coordinates
                String coords = String.format("%.1f %.1f %.1f", x, y, z);

                // Get the localized message
                String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", playerName, coords);
                MagicTP.LOGGER.debug("Localized message: " + localizedMessage);

                // Send the localized message to the player
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));
                MagicTP.LOGGER.debug("Sent localized message to player: " + localizedMessage);

                // Cancel the original raw message
                event.setCanceled(true);
                MagicTP.LOGGER.debug("Canceled original MagicTP message: " + rawMessage);
            } catch (NumberFormatException e) {
                MagicTP.LOGGER.error("Failed to parse MagicTP message: " + rawMessage, e);
            }
        } else {
            MagicTP.LOGGER.debug("Message is not a MagicTP-specific message. Allowing it to pass through.");
        }
    }
}