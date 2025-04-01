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
        String coords = String.format("%.1f %.1f %.1f", to.x, to.y, to.z);

        // Get the localized message
        String localizedMessage = LocaleRegexLoader.getLocalizedMessage("teleport_message", playerName, coords);

        // Send the localized message to the player
        player.sendSystemMessage(Component.literal(localizedMessage));

        // Log the localized message to the server console
        MagicTP.LOGGER.info("MagicTP: " + localizedMessage);
    }

    private static void suppressTeleportMessage(ServerPlayer player) {
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(Component.empty()));
        MagicTP.LOGGER.debug("Suppressed teleport message for player: " + player.getName().getString());
    }
}