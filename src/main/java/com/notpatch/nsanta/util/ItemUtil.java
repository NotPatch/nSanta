package com.notpatch.nsanta.util;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.notpatch.nsanta.NSanta;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.block.Chest;

public class ItemUtil {

    public static ItemStack getSkullFromBase64(String base64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (base64 == null || base64.isEmpty())
            return skull;


        if(NSanta.getInstance().getServer().getVersion().contains("1.21")){
            PlayerProfile profile = crateRandomProfile(base64);
            skullMeta.setOwnerProfile(profile);
            skull.setItemMeta(skullMeta);
            return skull;
        }else{
            GameProfile profile = new GameProfile(UUID.randomUUID(), UUID.randomUUID().toString().substring(0, 16));
            profile.getProperties().put("textures", new Property("textures", base64));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            skull.setItemMeta(skullMeta);
            return skull;
        }
    }

    public static List<ItemStack> getItemsFromList(List<String> rewards){
        Random random = new Random();
        if(rewards.isEmpty()) return new ArrayList<>();

        List<ItemStack> rewardItems = new ArrayList<>();
        for (String reward : rewards) {
            String[] rewardSplit = reward.split(":");

            int amount = Integer.parseInt(rewardSplit[1]);
            double chance = Double.parseDouble(rewardSplit[2].replace("%", ""));
            Material material = Material.getMaterial(rewardSplit[0]);

            ItemStack item = new ItemStack(material, amount);

            if (random.nextDouble() * 100 < chance) {
                rewardItems.add(item);
            }
        }
        return rewardItems;
    }

    public static PlayerProfile crateRandomProfile(String texture){
        final UUID uuid = UUID.randomUUID();
        PlayerProfile profile = NSanta.getInstance().getServer().createPlayerProfile(uuid, uuid.toString().substring(0, 16));

        PlayerTextures playerTextures = profile.getTextures();
        try {
            URL url = new URL("http://textures.minecraft.net/texture/" + texture);
            playerTextures.setSkin(url);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        profile.setTextures(playerTextures);

        return profile;
    }

    public static void fillChest(Inventory inv, List<ItemStack> items) {
        Random RANDOM = new Random();
        List<Integer> filled = Lists.newArrayList();

        inv.clear();

        int size = inv.getSize();

        for (int i = 0; i < items.size(); i++) {
            int slot = RANDOM.nextInt(size);

            while (filled.contains(slot)) {
                slot = RANDOM.nextInt(size);
            }

            filled.add(slot);
            inv.setItem(slot, items.get(i));
        }
    }


}
