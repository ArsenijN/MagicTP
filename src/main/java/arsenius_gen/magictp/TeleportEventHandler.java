package arsenius_gen.magictp;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class TeleportEventHandler {
    private static final Map<ServerPlayerEntity, Vec3d> lastPositions = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                Vec3d currentPos = player.getPos();
                Vec3d lastPos = lastPositions.get(player);

                double teleportThreshold = MagicTPConfig.teleportThreshold;

                if (lastPos != null && currentPos.distanceTo(lastPos) > teleportThreshold) {
                    sendMagicMessage(player, lastPos, currentPos);
                }

                lastPositions.put(player, currentPos);
            });
        });
    }

    private static void sendMagicMessage(ServerPlayerEntity player, Vec3d from, Vec3d to) {
        String coords = String.format("%.1f %.1f %.1f", to.x, to.y, to.z);
        player.sendMessage(Text.literal(player.getName().getString() + " teleported to " + coords), false);
    }
}