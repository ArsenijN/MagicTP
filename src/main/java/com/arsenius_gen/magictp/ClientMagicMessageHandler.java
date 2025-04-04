package com.arsenius_gen.magictp;

import net.minecraft.client.Minecraft;
// import net.minecraft.client.player.LocalPlayer;
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
                String decodedCoords = new String(Base64.getDecoder().decode(encodedPart));
    
                // Parse the compressed coordinates
                String[] parts = decodedCoords.split("\\|");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
    
                // Format the coordinates
                String coords = String.format("%.1f %.1f %.1f", x, y, z);
    
                // Get the localized message
                String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", "Unknown Player", coords);
    
                // Display the localized message
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal(localizedMessage));
                    MagicTP.LOGGER.debug("Decoded and displayed localized message: " + localizedMessage);
                }
    
                // Cancel the original raw message
                event.setCanceled(true);
            } catch (Exception e) {
                MagicTP.LOGGER.error("Failed to decode MagicTP message: " + rawMessage, e);
            }
        }
    }
}