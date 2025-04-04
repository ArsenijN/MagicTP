package com.arsenius_gen.magictp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LocaleRegexLoader {
    private static final String DEFAULT_LOCALE = "en_us";
    private static JsonObject cachedLocaleData = null;
    private static String cachedLocale = null;

    // Ensure the unsupported locale announcement on join is sent only once.
    private static boolean joinAnnouncementSent = false;

    public static boolean isLocaleSupported(String locale) {
        JsonObject localeData = loadLocaleFile(locale);
        return localeData != null;
    }

    private static JsonObject loadLocaleData() {
        String currentLocale = Minecraft.getInstance().getLanguageManager().getSelected();
        if (cachedLocale != null && cachedLocale.equals(currentLocale)) {
            return cachedLocaleData;
        }

        MagicTP.LOGGER.debug("Loading locale data for: " + currentLocale);
        JsonObject localeData = loadLocaleFile(currentLocale);
        if (localeData == null) {
            MagicTP.LOGGER.warn("Locale file for " + currentLocale + " not found. Falling back to default locale: " + DEFAULT_LOCALE);
            // On join (or first locale switch) announce unsupported locale if enabled.
            if (Minecraft.getInstance().player != null) {
                if (MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.get() && !joinAnnouncementSent) {
                    Minecraft.getInstance().player.sendSystemMessage(
                        Component.literal(getMissingLanguageMessage(currentLocale))
                    );
                    joinAnnouncementSent = true;
                }
            }
            localeData = loadLocaleFile(DEFAULT_LOCALE);
        }

        cachedLocale = currentLocale;
        cachedLocaleData = localeData;
        return localeData;
    }

    public static String getRegex(String key) {
        JsonObject localeData = loadLocaleData();
        if (localeData != null && localeData.has(key)) {
            return localeData.get(key).getAsString();
        } else {
            MagicTP.LOGGER.error("Failed to load regex for key: " + key);
            return "";
        }
    }

    public static String getMissingLanguageMessage(String lang) {
        return "This language (" + lang + ") isn't supported right now. " +
               "You can create /mods/magictp/lang/" + lang + ".json to add your own language, " +
               "or open an issue on https://github.com/ArsenijN/MagicTP/issues";
    }

    public static String getLocalizedMessage(String key, String player, String coords) {
        JsonObject localeData = loadLocaleData();
        if (localeData != null && localeData.has(key)) {
            String messageTemplate = localeData.get(key).getAsString();
            return messageTemplate.replace("{player}", player).replace("{coords}", coords);
        } else {
            MagicTP.LOGGER.error("Failed to load localized message for key: " + key);
            return "This language isn't supported. Defaulting to English.";
        }
    }

    private static JsonObject loadLocaleFile(String locale) {
        // Attempt to load external file from .minecraft/mods/magictp/lang/
        File externalFile = new File(Minecraft.getInstance().gameDirectory, "mods/magictp/lang/" + locale + ".json");
        if (externalFile.exists()) {
            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(externalFile), StandardCharsets.UTF_8)) {
                return new Gson().fromJson(reader, JsonObject.class);
            } catch (Exception e) {
                MagicTP.LOGGER.error("Failed to load external locale file: " + locale, e);
            }
        }

        // Fallback to internal resource file
        try (InputStreamReader reader = new InputStreamReader(
                LocaleRegexLoader.class.getResourceAsStream("/lang/" + locale + ".json"), StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            MagicTP.LOGGER.error("Failed to load internal locale file: " + locale, e);
            return null;
        }
    }
    public static void resetJoinAnnouncementFlag() {
        joinAnnouncementSent = false;
    }
    @Mod.EventBusSubscriber(modid = MagicTP.MOD_ID, value = Dist.CLIENT)
    public class ClientEventHandler {
        @SubscribeEvent
        public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            LocaleRegexLoader.resetJoinAnnouncementFlag();
        }

        @SubscribeEvent
        public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
            Minecraft.getInstance().execute(() -> {
                String currentLocale = Minecraft.getInstance().getLanguageManager().getSelected();
                if (!LocaleRegexLoader.isLocaleSupported(currentLocale)) {
                    if (MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.get()) {
                        Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal(LocaleRegexLoader.getMissingLanguageMessage(currentLocale))
                        );
                    }
                }
            });
        }
    }
}