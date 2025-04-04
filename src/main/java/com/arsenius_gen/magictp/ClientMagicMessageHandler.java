package com.arsenius_gen.magictp;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Base64;

@Mod.EventBusSubscriber(modid = "magictp", value = Dist.CLIENT)
public class ClientMagicMessageHandler {

    @SubscribeEvent
    public static void onChatMessage(ClientChatReceivedEvent event) {
        String rawMessage = event.getMessage().getString();
        MagicTP.LOGGER.debug("Received chat message: " + rawMessage);

        // Check for legacy format: "sku|0.0|0.0|0.0"
        if (rawMessage.contains("|") && !rawMessage.contains("[")) { 
            handleLegacyFormat(rawMessage, event);
            return;
        }

        // Handle the new Base64 format
        if (rawMessage.contains("was moved by magic!") || rawMessage.contains("[")) {
            try {
                // Extract the Base64-encoded part
                int startIndex = 3; // Adjust if needed
                int endIndex = rawMessage.indexOf("]");
                if (startIndex == 0 || endIndex == -1) {
                    MagicTP.LOGGER.error("Malformed MagicTP message: Missing Base64-encoded part.");
                    return;
                }

                // Strip brackets and decode the Base64 string
                String encodedPart = rawMessage.substring(startIndex, endIndex);
                MagicTP.LOGGER.debug("Extracted Base64 part: " + encodedPart);
                String decodedData = new String(Base64.getDecoder().decode(encodedPart));

                // Decompress the data
                String[] parts = decompressData(decodedData);
                if (parts.length != 4) {
                    MagicTP.LOGGER.error("Malformed MagicTP message: Incorrect number of parts.");
                    return;
                }

                displayTeleportMessage(parts[0], parts[1], parts[2], parts[3]);

                // Cancel the original raw message
                event.setCanceled(true);
            } catch (Exception e) {
                MagicTP.LOGGER.error("Failed to decode MagicTP message: " + rawMessage, e);
            }
        }
    }

    private static void handleLegacyFormat(String message, ClientChatReceivedEvent event) {
        String[] parts = message.split("\\|");
        if (parts.length != 4) {
            MagicTP.LOGGER.error("Malformed legacy MagicTP message: " + message);
            return;
        }

        String playerName = parts[0];
        String x = parts[1];
        String y = parts[2];
        String z = parts[3];

        displayTeleportMessage(playerName, x, y, z);

        // Cancel the original raw message
        event.setCanceled(true);
    }

    private static void displayTeleportMessage(String playerName, String x, String y, String z) {
        String coords = String.format("%.1f %.1f %.1f", Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
        String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", playerName, coords);

        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));
            MagicTP.LOGGER.debug("Displayed teleport message: " + localizedMessage);
        }
    }

    private static String[] decompressData(String compressedData) {
        // Split the encoded name and compressed coordinates
        String[] parts = compressedData.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Malformed compressed data: " + compressedData);
        }
    
        // Decode the player's name
        String playerName = new String(Base64.getDecoder().decode(parts[0]));
    
        // Decompress the coordinates
        StringBuilder decompressedCoords = new StringBuilder();

        for (char c : parts[1].toCharArray()) {
            int value = Integer.parseInt(String.valueOf(c), 16);
            char decodedChar = switch (value) {
                case 0 -> '0';
                case 1 -> '1';
                case 2 -> '2';
                case 3 -> '3';
                case 4 -> '4';
                case 5 -> '5';
                case 6 -> '6';
                case 7 -> '7';
                case 8 -> '8';
                case 9 -> '9';
                case 10 -> '.';
                case 11 -> '|';
                case 12 -> '-'; // Support for negative coordinates
                default -> throw new IllegalArgumentException("Invalid value in compressed coordinates: " + value);
            };
            decompressedCoords.append(decodedChar);
        }
    
        // Split the decompressed coordinates into x, y, z
        String[] coords = decompressedCoords.toString().split("\\|");
        if (coords.length != 3) {
            throw new IllegalArgumentException("Malformed decompressed coordinates: " + decompressedCoords);
        }
    
        // Return the player's name and coordinates
        return new String[]{playerName, coords[0], coords[1], coords[2]};
    }
}
