package arsenius_gen.magictp.client;


import arsenius_gen.magictp.MagicTPConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MagicTPConfigScreen {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.literal("MagicTP Configuration"));

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General Settings"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Teleport Threshold
        general.addEntry(entryBuilder.startFloatField(Text.literal("Teleport Threshold"), MagicTPConfig.teleportThreshold)
            .setDefaultValue(5.0f)
            .setSaveConsumer(newValue -> MagicTPConfig.teleportThreshold = newValue)
            .build());

        // Logging Threshold
        general.addEntry(entryBuilder.startFloatField(Text.literal("Logging Threshold"), MagicTPConfig.loggingThreshold)
            .setDefaultValue(1.0f)
            .setSaveConsumer(newValue -> MagicTPConfig.loggingThreshold = newValue)
            .build());

        builder.setSavingRunnable(MagicTPConfig::save);

        return builder.build();
    }
}