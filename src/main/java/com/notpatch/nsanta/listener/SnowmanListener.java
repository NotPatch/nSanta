package com.notpatch.nsanta.listener;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.event.EntityDeathByEntityEvent;
import com.notpatch.nsanta.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SnowmanListener implements Listener {

    @EventHandler
    public void onShear(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Configuration configuration = NSanta.getInstance().getConfig();
        if (!NSanta.getInstance().getConfig().getBoolean("advanced-snowman.enabled")) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getItem() == null) return;
        if (!e.getItem().getType().equals(Material.SHEARS)) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getType().equals(Material.PUMPKIN)) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entities : p.getNearbyEntities(3, 2, 3)) {
                        if (entities instanceof Snowman) {
                            entities.setCustomName(StringUtil.getColored("advanced-snowman.name").replace("%player%", p.getName()));
                            ((Snowman) entities).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(configuration.getDouble("advanced-snowman.heart") * 2);
                            ((Snowman) entities).setHealth(configuration.getDouble("advanced-snowman.heart") * 2);
                            NamespacedKey key = new NamespacedKey(NSanta.getInstance(), "creator");
                            entities.getPersistentDataContainer().set(key, PersistentDataType.STRING, p.getUniqueId().toString());
                        }
                    }
                }
            }.runTaskLater(NSanta.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onSnowballDamage(EntityDamageByEntityEvent e) {
        Entity attacker = e.getDamager();
        if (!attacker.getType().equals(EntityType.SNOWBALL)) {
            return;
        }

        Snowman snowman = (Snowman) getShooter(attacker);
        NamespacedKey key = new NamespacedKey(NSanta.getInstance(), "creator");

        if (!snowman.getPersistentDataContainer().has(key, PersistentDataType.STRING)){
            return;
        }

        String creatorId = snowman.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UUID creatorUUID = UUID.fromString(creatorId);
        Player creator = NSanta.getInstance().getServer().getPlayer(creatorUUID);

        if (creator == null) return;
        e.setDamage((e.getDamage() + 1) * NSanta.getInstance().getConfig().getInt("advanced-snowman.damage-multiplier"));
    }

    @EventHandler
    public void onSnowmanKillEntity(EntityDeathByEntityEvent e) {
        Entity attacker = e.getAttacker();
        Entity victim = e.getVictim();

        if (!(attacker instanceof Snowball)) return;

        Snowman snowman = (Snowman) getShooter(attacker);
        NamespacedKey key = new NamespacedKey(NSanta.getInstance(), "creator");

        if (!snowman.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return;
        }

        String creatorId = snowman.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        UUID creatorUUID = UUID.fromString(creatorId);
        Player creator = NSanta.getInstance().getServer().getPlayer(creatorUUID);

        if (creator == null) return;
        if(!NSanta.getInstance().getConfig().getBoolean("advanced-snowman.rewards.enabled")) return;
        List<String> monsters = NSanta.getInstance().getConfig().getStringList("advanced-snowman.rewards.monsters");
        if (monsters.contains(victim.getType().name())) {
            List<String> rewards = NSanta.getInstance().getConfig().getStringList("advanced-snowman.rewards.items-per-kill");
            if(rewards.isEmpty()) return;
            List<ItemStack> rewardItems = new ArrayList<>();
            for (String reward : rewards) {
                String[] rewardSplit = reward.split(":");
                Material material = Material.getMaterial(rewardSplit[0]);
                int amount = Integer.parseInt(rewardSplit[1]);
                ItemStack item = new ItemStack(material, amount);
                rewardItems.add(item);
            }
            rewardItems.forEach(item -> {creator.getInventory().addItem(item); });

            double money = NSanta.getInstance().getConfig().getDouble("advanced-snowman.rewards.money-per-kill");
            if(money > 0){
                NSanta.getInstance().getIntegrationManager().getVaultIntegration().getEconomy().depositPlayer(creator, NSanta.getInstance().getConfig().getInt("advanced-snowman.rewards.money-per-kill"));
            }
        }
    }

    public static Entity getShooter(Entity entity) {
        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            ProjectileSource source = projectile.getShooter();

            if (source instanceof Entity) {
                return (Entity) source;
            }
        }
        return null;
    }


}
