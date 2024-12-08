package com.notpatch.nsanta.model;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.util.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class LanguageLoader {

    HashMap<String, String> translationMap;

    public LanguageLoader(NSanta plugin) {
        if (translationMap == null) {
            translationMap = new HashMap<>();
        }
        File languageDirectory = new File(plugin.getDataFolder(), "languages/");
        File defaultLanguageFile = new File(plugin.getDataFolder(), "languages/en_US.yml");
        File trLanguageFile = new File(plugin.getDataFolder(), "languages/tr_TR.yml");

        if (!languageDirectory.isDirectory()) {
            languageDirectory.mkdirs();
        }

        if (!trLanguageFile.exists()) {
            plugin.saveResource("languages/tr_TR.yml", false);
        }

        if (!defaultLanguageFile.exists()) {
            plugin.saveResource("languages/en_US.yml", false);
        }

        if (plugin.getConfig().getString("locale") != null && plugin.getConfig().getString("locale").equals("tr_TR")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(trLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } else if (plugin.getConfig().getString("locale") != null && plugin.getConfig().getString("locale").equals("en_US")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "languages/" + plugin.getConfig().getString("locale") + ".yml"));
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } else {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        }
    }

    public String get(String path) {
        String translation = translationMap.get(path);
        if (translation == null) {
            return "Translation not found for path: " + path;
        }
        return StringUtil.hexColor(translation);
    }


    public void loadLangs() {
        File defaultLanguageFile = new File(NSanta.getInstance().getDataFolder(), "languages/en_US.yml");
        File trLanguageFile = new File(NSanta.getInstance().getDataFolder(), "languages/tr_TR.yml");

        if (NSanta.getInstance().getConfig().getString("locale") != null && NSanta.getInstance().getConfig().getString("locale").equals("tr_TR")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(trLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } else if (NSanta.getInstance().getConfig().getString("locale") != null && NSanta.getInstance().getConfig().getString("locale").equals("en_US")) {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(new File(NSanta.getInstance().getDataFolder(), "languages/" + NSanta.getInstance().getConfig().getString("locale") + ".yml"));
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        } else {
            FileConfiguration translations = YamlConfiguration.loadConfiguration(defaultLanguageFile);
            for (String translation : translations.getKeys(false)) {
                translationMap.put(translation, translations.getString(translation));
            }
        }
    }


}