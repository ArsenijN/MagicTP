package arsenius_gen.magictp;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientMagicMessageHandler {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("magictp", "chat_message"), (client, handler, buf, responseSender) -> {
            String message = buf.readString();
            client.execute(() -> {
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.sendMessage(Text.literal(message), false);
                }
            });
        });
    }
}