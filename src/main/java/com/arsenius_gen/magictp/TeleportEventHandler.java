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

        if (lastPos != null && currentPos.distanceTo(lastPos) > 5.0) { // Detects sudden large movement
            if (!isTeleportingOtherPlayer(player)) {
                suppressTeleportMessage(player);
                sendMagicMessage(player, lastPos, currentPos);
            }
        }

        lastPositions.put(player, currentPos);
    }

    private static boolean isTeleportingOtherPlayer(ServerPlayer player) {
        // Always return false to ensure the magic teleportation message is sent
        return false;
    }

    private static void sendMagicMessage(ServerPlayer player, Vec3 from, Vec3 to) {
        String playerName = player.getName().getString();
        double x = to.x;
        double y = to.y;
        double z = to.z;

        // Send raw data to all players on the server
        player.getServer().getPlayerList().getPlayers().forEach(p -> {
            p.sendSystemMessage(Component.literal(playerName + "|" + x + "|" + y + "|" + z));
        });

        // Log the raw data to the server console
        MainMod.LOGGER.info("MagicTP: " + playerName + " was moved to " + x + " " + y + " " + z);
    }

    private static void suppressTeleportMessage(ServerPlayer player) {
        // Suppress the default teleportation message
        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(Component.literal(""))); // Suppresses feedback messages

        // Disable command feedback for the player
        player.getServer().getCommands().getDispatcher().setConsumer((context, success, result) -> {
            if (context.getSource().getTextName().equals(player.getName().getString())) {
                context.getSource().sendSuccess(() -> Component.literal(""), false); // Suppress feedback output
            }
        });

        // Reset command feedback globally for the player
        player.getServer().getCommands().getDispatcher().setConsumer(null);
    }
}