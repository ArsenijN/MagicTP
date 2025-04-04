package com.arsenius_gen.magictp.client;

import com.arsenius_gen.magictp.MagicTPConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class MagicTPConfigScreen extends Screen {
    private EditBox teleportThresholdField; // Text field for teleport threshold
    private double teleportThreshold; // Current value of teleport threshold
    private boolean announceUnsupportedLocaleOnJoin;
    private boolean announceUnsupportedLocaleMagic;

    protected MagicTPConfigScreen() {
        super(Component.literal("MagicTP Configuration"));
    }

    @Override
    protected void init() {
        // Load the current value of teleport threshold
        teleportThreshold = MagicTPConfig.COMMON.teleportThreshold.get();
        announceUnsupportedLocaleOnJoin = MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.get();
        announceUnsupportedLocaleMagic = MagicTPConfig.COMMON.announceUnsupportedLocaleMagic.get();

        // Add a text field for teleport threshold
        teleportThresholdField = new EditBox(this.font, this.width / 2 - 50, this.height / 2 - 60, 100, 20, Component.literal("Teleport Threshold"));
        teleportThresholdField.setValue(String.valueOf(teleportThreshold));
        teleportThresholdField.setResponder(value -> {
            try {
                teleportThreshold = Double.parseDouble(value);
                MagicTPConfig.COMMON.teleportThreshold.set(teleportThreshold);
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        });
        this.addRenderableWidget(teleportThresholdField);

        // Add a decrement button
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            teleportThreshold = Math.max(1.0, teleportThreshold - 1.0); // Minimum value is 1.0
            teleportThresholdField.setValue(String.valueOf(teleportThreshold));
            MagicTPConfig.COMMON.teleportThreshold.set(teleportThreshold);
        }).bounds(this.width / 2 - 100, this.height / 2 - 60, 20, 20).build());

        // Add an increment button
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            teleportThreshold = Math.min(100.0, teleportThreshold + 1.0); // Maximum value is 100.0
            teleportThresholdField.setValue(String.valueOf(teleportThreshold));
            MagicTPConfig.COMMON.teleportThreshold.set(teleportThreshold);
        }).bounds(this.width / 2 + 80, this.height / 2 - 60, 20, 20).build());

        // Add "Announce Locale On Join" button
        this.addRenderableWidget(Button.builder(Component.literal("Announce Locale On Join: " + announceUnsupportedLocaleOnJoin),
            button -> {
                announceUnsupportedLocaleOnJoin = !announceUnsupportedLocaleOnJoin;
                button.setMessage(Component.literal("Announce Locale On Join: " + announceUnsupportedLocaleOnJoin));
                MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.set(announceUnsupportedLocaleOnJoin);
            }).bounds(this.width / 2 - 100, this.height / 2 - 20, 200, 20).build());

        // Add "Announce Locale Magic" button
        this.addRenderableWidget(Button.builder(Component.literal("Announce Locale Magic: " + announceUnsupportedLocaleMagic),
            button -> {
                announceUnsupportedLocaleMagic = !announceUnsupportedLocaleMagic;
                button.setMessage(Component.literal("Announce Locale Magic: " + announceUnsupportedLocaleMagic));
                MagicTPConfig.COMMON.announceUnsupportedLocaleMagic.set(announceUnsupportedLocaleMagic);
            }).bounds(this.width / 2 - 100, this.height / 2 + 10, 200, 20).build());

        // Check if extended configurables should be shown
        if (MagicTPConfig.COMMON.showExtendedConfigurables.get()) {
            // Add "Magic Message Global" button
            this.addRenderableWidget(Button.builder(Component.literal("Magic Message Global: " + MagicTPConfig.COMMON.magicMessageGlobal.get()),
                button -> {
                    boolean currentValue = MagicTPConfig.COMMON.magicMessageGlobal.get();
                    MagicTPConfig.COMMON.magicMessageGlobal.set(!currentValue);
                    button.setMessage(Component.literal("Magic Message Global: " + !currentValue));
                }).bounds(this.width / 2 - 100, this.height / 2 + 40, 200, 20).build());

            // Add "Logging Threshold" field
            EditBox loggingThresholdField = new EditBox(this.font, this.width / 2 - 50, this.height / 2 + 70, 100, 20, Component.literal("Logging Threshold"));
            loggingThresholdField.setValue(String.valueOf(MagicTPConfig.COMMON.loggingThreshold.get()));
            loggingThresholdField.setResponder(value -> {
                try {
                    double loggingThreshold = Double.parseDouble(value);
                    MagicTPConfig.COMMON.loggingThreshold.set(loggingThreshold);
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            });
            this.addRenderableWidget(loggingThresholdField);
        }

        // Add a "Done" button to close the screen
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> this.onClose())
            .bounds(this.width / 2 - 50, this.height / 2 + 100, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Magic Threshold: Adjust teleport sensitivity", this.width / 2 - 100, this.height / 2 - 90, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new MagicTPConfigScreen()));
    }
}