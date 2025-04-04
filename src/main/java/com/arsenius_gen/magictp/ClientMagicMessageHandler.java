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

        // Check if the message is a MagicTP-specific encoded message
        if (rawMessage.startsWith("[MC") && rawMessage.contains("]")) {
            try {
                // Extract the Base64-encoded part
                String encodedPart = rawMessage.substring(3, rawMessage.indexOf("]"));
                String decodedData = new String(Base64.getDecoder().decode(encodedPart));

                // Check if the message is in the old format
                if (decodedData.contains("|")) {
                    String[] parts = decodedData.split("\\|");
                    if (parts.length == 4) {
                        handleOldFormatMessage(parts);
                        event.setCanceled(true);
                        return;
                    }
                }

                // Handle the new compressed format
                String decompressedData = decompressData(decodedData);
                String[] parts = decompressedData.split("\\|");
                if (parts.length == 4) {
                    handleNewFormatMessage(parts);
                    event.setCanceled(true);
                } else {
                    MagicTP.LOGGER.error("Malformed MagicTP message: " + decompressedData);
                }
            } catch (Exception e) {
                MagicTP.LOGGER.error("Failed to decode MagicTP message: " + rawMessage, e);
            }
        }
    }

    private static void handleOldFormatMessage(String[] parts) {
        try {
            String playerName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            // Format the coordinates
            String coords = String.format("%.1f %.1f %.1f", x, y, z);

            // Get the localized message
            String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", playerName, coords);

            // Display the localized message
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));
                MagicTP.LOGGER.debug("Decoded and displayed localized message (old format): " + localizedMessage);
            }
        } catch (Exception e) {
            MagicTP.LOGGER.error("Failed to handle old format MagicTP message", e);
        }
    }

    private static void handleNewFormatMessage(String[] parts) {
        try {
            String playerName = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            // Format the coordinates
            String coords = String.format("%.1f %.1f %.1f", x, y, z);

            // Get the localized message
            String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", playerName, coords);

            // Display the localized message
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));
                MagicTP.LOGGER.debug("Decoded and displayed localized message (new format): " + localizedMessage);
            }
        } catch (Exception e) {
            MagicTP.LOGGER.error("Failed to handle new format MagicTP message", e);
        }
    }

    private static String decompressData(String compressedData) {
        StringBuilder decompressed = new StringBuilder();
        for (int i = 0; i < compressedData.length(); i++) {
            char c = compressedData.charAt(i);
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
                default -> throw new IllegalArgumentException("Invalid value: " + value);
            };
            decompressed.append(decodedChar);
        }
        return decompressed.toString();
    }
}