package com.arsenius_gen.magictp.commands;

import com.arsenius_gen.magictp.MagicTPConfig;
import com.arsenius_gen.magictp.MagicTP;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MagicTPCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("magictp")
            .then(Commands.literal("set")
                .then(Commands.literal("teleport_threshold")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg(1.0, 100.0))
                        .executes(context -> {
                            double value = DoubleArgumentType.getDouble(context, "value");
                            MagicTPConfig.COMMON.teleportThreshold.set(value);
                            context.getSource().sendSuccess(() -> Component.literal("Teleport threshold set to " + value), true);
                            MagicTP.LOGGER.info("Teleport threshold set to " + value);
                            return 1;
                        })))
                .then(Commands.literal("logging_threshold")
                    .then(Commands.argument("value", DoubleArgumentType.doubleArg(0.1, 10.0))
                        .executes(context -> {
                            double value = DoubleArgumentType.getDouble(context, "value");
                            MagicTPConfig.COMMON.loggingThreshold.set(value);
                            context.getSource().sendSuccess(() -> Component.literal("Logging threshold set to " + value), true);
                            MagicTP.LOGGER.info("Logging threshold set to " + value);
                            return 1;
                        })))));
    }
}