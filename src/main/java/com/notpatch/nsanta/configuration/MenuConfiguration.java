package com.notpatch.nsanta.configuration;

import com.notpatch.nsanta.NSanta;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MenuConfiguration {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup(){
        file = new File(NSanta.getInstance().getDataFolder(), "menu.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                System.out.println("Couldn't create file");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        customFile.addDefault("title", "&cChristmas Tree");
        customFile.addDefault("filler-item.material", "GRAY_STAINED_GLASS_PANE");
        customFile.addDefault("filler-item.name", " ");
        customFile.addDefault("filler-item.slots", Arrays.asList(0,1,2,3,4,5,6,7,8,9,18,27,36,45,46,47,48,49,50,51,52,53,17,26,35,44));

        customFile.addDefault("ingredients.name", "&a%integredient%");
        customFile.addDefault("ingredients.lore", Arrays.asList(" ", "&7Tree need %amount% more", "&7of this ingredient!", " "));
        save();
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            System.out.println("Couldn't save file");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}