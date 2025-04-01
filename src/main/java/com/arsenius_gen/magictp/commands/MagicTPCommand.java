package com.arsenius_gen.magictp.commands;

import com.arsenius_gen.magictp.MagicTPConfig;
import com.arsenius_gen.magictp.MagicTP;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MagicTPCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("magictp")
            .then(Commands.literal("set")
                .then(Commands.literal("teleport_threshold")
                    .then(Commands.argument("value", FloatArgumentType.floatArg(1.0f, 100.0f))
                        .executes(context -> {
                            float value = FloatArgumentType.getFloat(context, "value");
                            MagicTPConfig.COMMON.teleportThreshold.set((double) value);
                            context.getSource().sendSuccess(() -> Component.literal("Teleport threshold set to " + value), true);
                            MagicTP.LOGGER.info("Teleport threshold set to " + value);
                            return 1;
                        })))
                .then(Commands.literal("logging_threshold")
                    .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 10.0f))
                        .executes(context -> {
                            float value = FloatArgumentType.getFloat(context, "value");
                            MagicTPConfig.COMMON.loggingThreshold.set((double) value);
                            context.getSource().sendSuccess(() -> Component.literal("Logging threshold set to " + value), true);
                            MagicTP.LOGGER.info("Logging threshold set to " + value);
                            return 1;
                        }))))
            .then(Commands.literal("get")
                .then(Commands.argument("variable", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("teleport_threshold");
                        builder.suggest("logging_threshold");
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        String variable = StringArgumentType.getString(context, "variable");
                        String response;

                        switch (variable) {
                            case "teleport_threshold" -> {
                                double teleportThreshold = MagicTPConfig.COMMON.teleportThreshold.get();
                                response = "Teleport threshold is currently set to " + teleportThreshold;
                            }
                            case "logging_threshold" -> {
                                double loggingThreshold = MagicTPConfig.COMMON.loggingThreshold.get();
                                response = "Logging threshold is currently set to " + loggingThreshold;
                            }
                            default -> {
                                response = "Unknown variable: " + variable + ". Valid variables are: teleport_threshold, logging_threshold.";
                            }
                        }

                        context.getSource().sendSuccess(() -> Component.literal(response), true);
                        return 1;
                    }))));
    }
}