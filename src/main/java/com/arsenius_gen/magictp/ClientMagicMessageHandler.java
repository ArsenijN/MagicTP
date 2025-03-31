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

        // Check if the message is a MagicTP message
        if (rawMessage.contains("|") && rawMessage.split("\\|").length == 4) {
            try {
                String[] parts = rawMessage.split("\\|");
                String playerName = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);

                // Localize the message based on the client's language
                String localizedMessage = getLocalizedMessage(playerName, x, y, z);
                Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));

                // Cancel the original raw message
                event.setCanceled(true);
            } catch (NumberFormatException e) {
                MagicTP.LOGGER.error("Failed to parse MagicTP message: " + rawMessage, e);
            }
        }
    }

    private static String getLocalizedMessage(String playerName, double x, double y, double z) {
        String coords = String.format("%.1f %.1f %.1f", x, y, z);
        String lang = Minecraft.getInstance().getLanguageManager().getSelected(); // Fixed getCode() issue

        return switch (lang) {
            case "uk_ua" -> playerName + " було магічно переміщено до " + coords;
            case "ru_ru" -> playerName + " был перемещён при помощи магии в " + coords;
            default -> playerName + " was moved with magic to " + coords;
        };
    }

    public static void displayMagicMessage(String playerName, double x, double y, double z) {
        String message = playerName + " was moved with magic to " + String.format("%.1f %.1f %.1f", x, y, z);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(message));
    }
}