package com.arsenius_gen.magictp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.client.event.ScreenEvent;
import net.minecraft.client.gui.screens.LanguageSelectScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocaleRegexLoader {
    private static final String DEFAULT_LOCALE = "en_us";
    private static String[] internalLocales = {"en_us", "uk_ua", "ry_ua", "ru_ru"}; // Add any other built-in locales here
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
                        getMissingLanguageMessage(currentLocale)
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

    public static Component getMissingLanguageMessage(String lang) {
        // Get list of available locales
        List<String> availableLocales = getAvailableLocales();
        StringBuilder localesText = new StringBuilder();
        
        if (!availableLocales.isEmpty()) {
            localesText.append("Available locales: ");
            for (int i = 0; i < availableLocales.size(); i++) {
                localesText.append(availableLocales.get(i));
                if (i < availableLocales.size() - 1) {
                    localesText.append(", ");
                }
            }
        } else {
            localesText.append("No additional locales found");
        }

        return Component.literal("")
            .append("MagicTP: This language (" + lang + ") isn't supported right now. ")
            .append("You can create /mods/magictp/lang/" + lang + ".json to add your own language, ")
            .append("or open an issue on ")
            .append(Component.literal("GitHub")
                .withStyle(style -> style
                    .withColor(0xcef2ac)
                    .withClickEvent(new net.minecraft.network.chat.ClickEvent(
                        net.minecraft.network.chat.ClickEvent.Action.OPEN_URL,
                        "https://github.com/ArsenijN/MagicTP/issues"
                    ))
                    .withHoverEvent(new net.minecraft.network.chat.HoverEvent(
                        net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                        Component.literal("Click to open the MagicTP GitHub issue page")
                    )))
            )
            .append(Component.literal(" so we will add it in later builds. Thanks!\n"))
            .append(Component.literal(localesText.toString()));
    }

    /**
     * Get a list of all available locales from both internal resources and external directories
     */
    public static List<String> getAvailableLocales() {
        List<String> locales = new ArrayList<>();
        
        // Add internal locales from resources
        try {
            File modsDir = new File(Minecraft.getInstance().gameDirectory, "mods");
            File langDir = new File(modsDir, "magictp/lang");
            
            // Check external files first
            if (langDir.exists() && langDir.isDirectory()) {
                File[] files = langDir.listFiles((dir, name) -> name.endsWith(".json"));
                if (files != null) {
                    for (File file : files) {
                        String localeName = file.getName().replace(".json", "");
                        locales.add(localeName);
                    }
                }
            }
            
            // Add internal resource locales
            // This is a simplified approach - you may need to adjust based on your actual
            // resource loading mechanism
            // String[] internalLocales = {"en_us"}; // Add any other built-in locales
            for (String locale : internalLocales) {
                if (!locales.contains(locale)) {
                    locales.add(locale);
                }
            }
        } catch (Exception e) {
            MagicTP.LOGGER.error("Failed to get available locales", e);
        }
        
        return locales;
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
    public static class ClientEventHandler {
        // Previous cached locale to detect changes
        private static String previousLocale = null;
        
        @SubscribeEvent
        public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
            LocaleRegexLoader.resetJoinAnnouncementFlag();
        }

        @SubscribeEvent
        public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
            Minecraft.getInstance().execute(() -> {
                String currentLocale = Minecraft.getInstance().getLanguageManager().getSelected();
                previousLocale = currentLocale;
                if (!LocaleRegexLoader.isLocaleSupported(currentLocale)) {
                    if (MagicTPConfig.COMMON.announceUnsupportedLocaleOnJoin.get()) {
                        Minecraft.getInstance().player.sendSystemMessage(
                            LocaleRegexLoader.getMissingLanguageMessage(currentLocale)
                        );
                    }
                }
            });
        }
        
        @SubscribeEvent
        public static void onLanguageScreenClosed(ScreenEvent.Closing event) {
            // Check if the screen being closed is the language selection screen
            if (event.getScreen() instanceof LanguageSelectScreen && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().execute(() -> {
                    String currentLocale = Minecraft.getInstance().getLanguageManager().getSelected();
                    
                    // Check if locale has changed and is different from previous
                    if (previousLocale != null && !currentLocale.equals(previousLocale)) {
                        // Reset cache to force reload
                        cachedLocale = null;
                        cachedLocaleData = null;
                        
                        // If the new locale is not supported, send message
                        if (!LocaleRegexLoader.isLocaleSupported(currentLocale)) {
                            Minecraft.getInstance().player.sendSystemMessage(
                                LocaleRegexLoader.getMissingLanguageMessage(currentLocale)
                            );
                        }
                    }
                    
                    // Update previous locale
                    previousLocale = currentLocale;
                });
            }
        }
    }
}