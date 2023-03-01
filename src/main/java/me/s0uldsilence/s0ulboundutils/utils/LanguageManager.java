package me.s0uldsilence.s0ulboundutils.utils;

import me.s0uldsilence.s0ulboundutils.S0ulboundUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LanguageManager {

    private static Map<String, FileConfiguration> languageFiles = new HashMap<>();

    public static void init() {
        File langDirectory = new File(S0ulboundUtils.getInstance().getDataFolder(), "lang");
        if (!langDirectory.exists()) {
            langDirectory.mkdirs();
        }

        // Copy all lang files from plugin JAR to langDirectory
        InputStream langResource;
        String[] languages = {"en_US", "de_DE", "fr_FR"}; // Replace with your list of supported languages
        for (String language : languages) {
            String resourceName = "lang/lang_" + language + ".yml";
            langResource = S0ulboundUtils.getInstance().getResource(resourceName);
            if (langResource == null) {
                S0ulboundUtils.getInstance().getLogger().warning("Missing language file: " + resourceName);
                continue;
            }
            File langFile = new File(langDirectory, "lang_" + language + ".yml");
            if (langFile.exists()) {
                langFile.delete();
            }
            try {
                Files.copy(langResource, langFile.toPath());
                S0ulboundUtils.getInstance().getLogger().info("Replaced language file " + langFile.getName());
            } catch (IOException ex) {
                S0ulboundUtils.getInstance().getLogger().log(Level.SEVERE, "Error copying language file " + resourceName + " to " + langFile.getAbsolutePath(), ex);
                continue;
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(langFile);
            languageFiles.put(language, config);
        }
    }

    public static String getMessage(String path) {
        String language = ConfigManager.getString("language");
        return getMessage(language, path);
    }

    public static String getMessage(String language, String path) {
        FileConfiguration config = languageFiles.get(language);
        if (config != null && config.contains(path)) {
            return config.getString(path);
        } else {
            S0ulboundUtils.getInstance().getLogger().warning("Missing language key " + path + " in " + language + " language file");
            return "Missing message for " + path;
        }
    }

    private static void saveResource(String resourcePath, File outputFile) {
        InputStream resourceStream = S0ulboundUtils.getInstance().getResource(resourcePath);
        if (resourceStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try {
            Files.copy(resourceStream, outputFile.toPath());
        } catch (IOException ex) {
            S0ulboundUtils.getInstance().getLogger().log(Level.SEVERE, "Error saving resource " + resourcePath + " to " + outputFile.getAbsolutePath(), ex);
        }
    }

}