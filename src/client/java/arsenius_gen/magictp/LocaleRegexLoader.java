package arsenius_gen.magictp;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClient;

public class LocaleRegexLoader {
    private static final String DEFAULT_LOCALE = "en_us";
    private static JsonObject cachedLocaleData = null;
    private static String cachedLocale = null;

    private static JsonObject loadLocaleData() {
        String currentLocale = MinecraftClient.getInstance().getLanguageManager().getLanguage(); // Directly use getLanguage()
        if (cachedLocale != null && cachedLocale.equals(currentLocale)) {
            return cachedLocaleData; // Return cached data if the locale hasn't changed
        }
    
        MagicTP.LOGGER.debug("Loading locale data for: " + currentLocale);
        JsonObject localeData = loadLocaleFile(currentLocale);
        if (localeData == null) {
            MagicTP.LOGGER.warn("Locale file for " + currentLocale + " not found. Falling back to default locale: " + DEFAULT_LOCALE);
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
            return ""; // Return an empty regex if the key is not found
        }
    }

    public static String getLocalizedMessage(String key, String player, String coords) {
        JsonObject localeData = loadLocaleData();
        if (localeData != null && localeData.has(key)) {
            String messageTemplate = localeData.get(key).getAsString();
            return messageTemplate.replace("{player}", player).replace("{coords}", coords);
        } else {
            MagicTP.LOGGER.error("Failed to load localized message for key: " + key);
            return player + " was moved with magic to " + coords; // Fallback message
        }
    }

    private static JsonObject loadLocaleFile(String locale) {
        try (InputStreamReader reader = new InputStreamReader(
                LocaleRegexLoader.class.getResourceAsStream("/lang/" + locale + ".json"), StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            MagicTP.LOGGER.error("Failed to load locale file: " + locale, e);
            return null;
        }
    }
}