package arsenius_gen.magictp;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import arsenius_gen.magictp.commands.MagicTPCommand;

public class MagicTP implements ModInitializer {
    public static final String MOD_ID = "magictp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("MagicTP Mod has loaded!");

        // Register commands
        MagicTPCommand.register();

        // Initialize configuration
        MagicTPConfig.init();

        // Register event handlers
        TeleportEventHandler.register();
    }
}