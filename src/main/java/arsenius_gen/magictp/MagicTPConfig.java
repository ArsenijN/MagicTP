package arsenius_gen.magictp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MagicTPConfig {
    public static final File CONFIG_FILE = new File("config/magictp.json");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static float teleportThreshold = 5.0f;
    public static float loggingThreshold = 1.0f;

    public static void init() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                MagicTPConfig config = GSON.fromJson(reader, MagicTPConfig.class);
                teleportThreshold = config.teleportThreshold;
                loggingThreshold = config.loggingThreshold;
            } catch (IOException e) {
                MagicTP.LOGGER.error("Failed to load config", e);
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(new MagicTPConfig(), writer);
        } catch (IOException e) {
            MagicTP.LOGGER.error("Failed to save config", e);
        }
    }
}