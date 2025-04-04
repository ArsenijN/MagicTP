package com.arsenius_gen.magictp.commands;

import com.arsenius_gen.magictp.MagicTPConfig;
import com.arsenius_gen.magictp.MagicTP;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.BoolArgumentType;

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
                        })))
                .then(Commands.literal("announce_locale_on_join")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean value = BoolArgumentType.getBool(context, "value");
                            MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.set(value);
                            context.getSource().sendSuccess(() -> Component.literal("Announce Locale On Join set to " + value), true);
                            return 1;
                        })))
                .then(Commands.literal("announce_locale_magic")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> {
                            boolean value = BoolArgumentType.getBool(context, "value");
                            MagicTPConfig.COMMON.announceUnsupportedLocaleMagic.set(value);
                            context.getSource().sendSuccess(() -> Component.literal("Announce Locale Magic set to " + value), true);
                            return 1;
                        }))))
            .then(Commands.literal("get")
                .then(Commands.argument("variable", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        builder.suggest("teleport_threshold");
                        builder.suggest("logging_threshold");
                        builder.suggest("announce_locale_on_join");
                        builder.suggest("announce_locale_magic");
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
                            case "announce_locale_on_join" -> {
                                boolean value = MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.get();
                                response = "Announce Locale On Join is currently set to " + value;
                            }
                            case "announce_locale_magic" -> {
                                boolean value = MagicTPConfig.COMMON.announceUnsupportedLocaleMagic.get();
                                response = "Announce Locale Magic is currently set to " + value;
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