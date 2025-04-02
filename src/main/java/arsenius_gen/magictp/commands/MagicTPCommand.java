package arsenius_gen.magictp.commands;

import arsenius_gen.magictp.MagicTPConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class MagicTPCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("magictp")
                .then(CommandManager.literal("set")
                    .then(CommandManager.literal("teleport_threshold")
                        .then(CommandManager.argument("value", FloatArgumentType.floatArg(1.0f, 100.0f))
                            .executes(context -> {
                                float value = FloatArgumentType.getFloat(context, "value");
                                MagicTPConfig.teleportThreshold = value;
                                MagicTPConfig.save();
                                context.getSource().sendFeedback(() -> Text.literal("Teleport threshold set to " + value), false);
                                return 1;
                            })))
                    .then(CommandManager.literal("logging_threshold")
                        .then(CommandManager.argument("value", FloatArgumentType.floatArg(0.1f, 10.0f))
                            .executes(context -> {
                                float value = FloatArgumentType.getFloat(context, "value");
                                MagicTPConfig.loggingThreshold = value;
                                MagicTPConfig.save();
                                context.getSource().sendFeedback(() -> Text.literal("Logging threshold set to " + value), false);
                                return 1;
                            }))))
                .then(CommandManager.literal("get")
                    .then(CommandManager.argument("variable", StringArgumentType.word())
                        .executes(context -> {
                            String variable = StringArgumentType.getString(context, "variable");
                            String response;

                            switch (variable) {
                                case "teleport_threshold" -> response = "Teleport threshold is currently set to " + MagicTPConfig.teleportThreshold;
                                case "logging_threshold" -> response = "Logging threshold is currently set to " + MagicTPConfig.loggingThreshold;
                                default -> response = "Unknown variable: " + variable;
                            }

                            context.getSource().sendFeedback(() -> Text.literal(response), false);
                            return 1;
                        }))));
        });
    }
}