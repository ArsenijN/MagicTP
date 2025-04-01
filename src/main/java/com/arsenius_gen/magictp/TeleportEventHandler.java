package com.arsenius_gen.magictp;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class TeleportEventHandler {

    private static final Map<ServerPlayer, Vec3> lastPositions = new HashMap<>();
    private static final double TELEPORT_THRESHOLD = 5.0; // Distance threshold for teleportation
    private static final double LOGGING_THRESHOLD = 1.0; // Distance threshold for logging

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player) || event.phase != TickEvent.Phase.END) {
            return;
        }

        Vec3 currentPos = player.position();
        Vec3 lastPos = lastPositions.get(player);

        // Log only if the player has moved more than the logging threshold
        if (lastPos == null || currentPos.distanceTo(lastPos) > LOGGING_THRESHOLD) {
            MagicTP.LOGGER.debug("The current position of " + player.getName().getString() + " is: " + currentPos);
            MagicTP.LOGGER.debug("The last position of " + player.getName().getString() + " is: " + lastPos);
        }

        // Detect teleportation (large movement)
        if (lastPos != null && currentPos.distanceTo(lastPos) > TELEPORT_THRESHOLD) {
            suppressTeleportMessage(player);
            sendMagicMessage(player, lastPos, currentPos);
        }

        // Update the last position
        lastPositions.put(player, currentPos);
    }

    private static void sendMagicMessage(ServerPlayer player, Vec3 from, Vec3 to) {
        String playerName = player.getName().getString();
        double x = to.x;
        double y = to.y;
        double z = to.z;

        // Send raw data to all players on the server
        if (player.getServer() != null) {
            player.getServer().getPlayerList().getPlayers().forEach(p -> {
                p.sendSystemMessage(Component.literal(playerName + "|" + x + "|" + y + "|" + z));
            });
        }

        // Log the raw data to the server console
        MagicTP.LOGGER.info("MagicTP: " + playerName + " was moved to " + x + " " + y + " " + z);
    }

    private static void suppressTeleportMessage(ServerPlayer player) {
        // Suppress the default teleportation message for this player only
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(Component.literal("")));
        MagicTP.LOGGER.debug("Suppressed teleport message for player: " + player.getName().getString());
    }
}