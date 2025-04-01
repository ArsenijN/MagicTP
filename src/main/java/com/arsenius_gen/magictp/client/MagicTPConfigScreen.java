package com.arsenius_gen.magictp.client;

import javax.annotation.Nonnull;

import com.arsenius_gen.magictp.MagicTPConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class MagicTPConfigScreen extends Screen {

    protected MagicTPConfigScreen() {
        super(Component.literal("MagicTP Configuration"));
    }

    @Override
    protected void init() {
        // Add a button to modify the teleport threshold
        this.addRenderableWidget(Button.builder(
            Component.literal("Teleport Threshold: " + MagicTPConfig.COMMON.teleportThreshold.get()),
            button -> {
                double newValue = MagicTPConfig.COMMON.teleportThreshold.get() + 1.0;
                MagicTPConfig.COMMON.teleportThreshold.set(newValue);
                button.setMessage(Component.literal("Teleport Threshold: " + newValue));
            }
        ).bounds(this.width / 2 - 100, this.height / 2 - 20, 200, 20).build());

        // Add a "Done" button to close the screen
        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            button -> this.onClose()
        ).bounds(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build());
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
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