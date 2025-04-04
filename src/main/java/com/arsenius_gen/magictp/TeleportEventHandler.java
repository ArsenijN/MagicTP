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
        boolean discloseCoords = MagicTPConfig.COMMON.playerDisclosure.get();
    
        String messageText;
        if (discloseCoords) {
            // Encode coordinates
            String compressedData = compressData(playerName, to);
            String encodedData = Base64.getEncoder().encodeToString(compressedData.getBytes());
    
            messageText = "[MC" + encodedData + "]\n" + playerName + " was moved by magic!";
        } else {
            // Use the alternative message
            messageText = "[MC" + "]\n" + playerName + " was moved by magic!";
        }
    
        // Create the global message
        Component globalMessage = Component.literal(messageText)
            .append(Component.literal(" To view more, install MagicTP from ")
                .append(Component.literal("Modrinth")
                    .withStyle(style -> style
                        .withColor(0x00FF00)
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
    
        // Send message
        player.getServer().getPlayerList().broadcastSystemMessage(globalMessage, false);
    
        MagicTP.LOGGER.info("MagicTP: Sent teleport message: " + messageText);
    }
    

    private static void suppressTeleportMessage(ServerPlayer player) {
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(Component.empty()));
        MagicTP.LOGGER.debug("Suppressed teleport message for player: " + player.getName().getString());
    }

    private static String compressData(String playerName, Vec3 coords) {
        String encodedName = Base64.getEncoder().encodeToString(playerName.getBytes());
        String x = String.format("%.1f", coords.x);
        String y = String.format("%.1f", coords.y);
        String z = String.format("%.1f", coords.z);

        StringBuilder compressedCoords = new StringBuilder();
        for (char c : (x + "|" + y + "|" + z).toCharArray()) {
            int value = switch (c) {
                case '0' -> 0;
                case '1' -> 1;
                case '2' -> 2;
                case '3' -> 3;
                case '4' -> 4;
                case '5' -> 5;
                case '6' -> 6;
                case '7' -> 7;
                case '8' -> 8;
                case '9' -> 9;
                case '.' -> 10;
                case '|' -> 11;
                case '-' -> 12;
                default -> throw new IllegalArgumentException("Invalid character in coordinates: " + c);
            };
            compressedCoords.append(Integer.toHexString(value));
        }
        return encodedName + ":" + compressedCoords;
    }
}
