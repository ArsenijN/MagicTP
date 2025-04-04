package com.arsenius_gen.magictp;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@Mod.EventBusSubscriber
public class TeleportEventHandler {

    private static final Map<ServerPlayer, Vec3> lastPositions = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player) || event.phase != TickEvent.Phase.END) {
            return;
        }

        Vec3 currentPos = player.position();
        Vec3 lastPos = lastPositions.get(player);

        double teleportThreshold = MagicTPConfig.COMMON.teleportThreshold.get();
        double loggingThreshold = MagicTPConfig.COMMON.loggingThreshold.get();

        // Detect teleportation (large movement)
        if (lastPos != null && currentPos.distanceTo(lastPos) > teleportThreshold) {
            suppressTeleportMessage(player);
            sendMagicMessage(player, lastPos, currentPos);
        }

        // Log only if debugging is enabled
        if (MagicTP.DEBUG && (lastPos == null || currentPos.distanceTo(lastPos) > loggingThreshold)) {
            MagicTP.LOGGER.debug("The current position of " + player.getName().getString() + " is: " + currentPos);
            MagicTP.LOGGER.debug("The last position of " + player.getName().getString() + " is: " + lastPos);
        }

        // Update the last position
        lastPositions.put(player, currentPos);
    }

    private static void sendMagicMessage(ServerPlayer player, Vec3 from, Vec3 to) {
        String playerName = player.getName().getString();
    
        // Compress and encode the player's name and coordinates
        String compressedData = compressData(playerName, to);
        String encodedData = Base64.getEncoder().encodeToString(compressedData.getBytes());
    
        // Create the global message with a clickable link
        Component globalMessage = Component.literal("[MC" + encodedData + "]\n")
            .append(Component.literal(playerName + " was moved by magic! To view what coordinates and translate this message, install MagicTP from ")
                .append(Component.literal("Modrinth")
                    .withStyle(style -> style
                        .withColor(0x00FF00) // Green color for the link
                        .withClickEvent(new net.minecraft.network.chat.ClickEvent(
                            net.minecraft.network.chat.ClickEvent.Action.OPEN_URL,
                            "https://modrinth.com/mod/magictp"
                        ))
                        .withHoverEvent(new net.minecraft.network.chat.HoverEvent(
                            net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                            Component.literal("Click to open the MagicTP Modrinth page")
                        ))
                    )
                )
            );
    
        // Send the global message to all players
        player.getServer().getPlayerList().broadcastSystemMessage(globalMessage, false);
    
        // Log the message to the server console
        MagicTP.LOGGER.info("MagicTP: Sent encoded message: " + globalMessage.getString());
    }

    private static void suppressTeleportMessage(ServerPlayer player) {
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(Component.empty()));
        MagicTP.LOGGER.debug("Suppressed teleport message for player: " + player.getName().getString());
    }

    // Compress the player's name and coordinates into a compact string
    private static String compressData(String playerName, Vec3 coords) {
        String x = String.format("%.1f", coords.x);
        String y = String.format("%.1f", coords.y);
        String z = String.format("%.1f", coords.z);
        return playerName + "|" + x + "|" + y + "|" + z;
    }
}