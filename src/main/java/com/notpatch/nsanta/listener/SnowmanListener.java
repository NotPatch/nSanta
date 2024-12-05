package com.notpatch.nsanta.listener;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class SnowmanSpawnListener implements Listener {

    private final HashMap<UUID, UUID> snowmanCreators = new HashMap<>();

    @EventHandler
    public void onShear(PlayerInteractEvent e){
        Player p =  e.getPlayer();
        Configuration configuration = NSanta.getInstance().getConfig();
        if(!NSanta.getInstance().getConfig().getBoolean("advanced-snowman.enabled")) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(e.getItem() == null) return;
        if(!e.getItem().getType().equals(Material.SHEARS)) return;
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(e.getClickedBlock().getType().equals(Material.PUMPKIN)){

            new BukkitRunnable(){
                @Override
                public void run() {
                    for(Entity entities : p.getNearbyEntities(3,2,3)){
                        if(entities instanceof Snowman){
                            entities.setCustomName(StringUtil.getColored("advanced-snowman.name").replace("%player%", p.getName()));
                            ((Snowman) entities).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(configuration.getDouble("advanced-snowman.health")*2);
                            ((Snowman) entities).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(configuration.getDouble("advanced-snowman.strength"));
                            ((Snowman) entities).setHealth(configuration.getDouble("advanced-snowman.health")*2);

                            snowmanCreators.put(p.getUniqueId(), entities.getUniqueId());
                        }
                    }
                }
            }.runTaskLater(NSanta.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Snowman) {
            Player p = (Player) e.getDamager();
            Snowman snowman = (Snowman) e.getEntity();
            if(snowmanCreators.containsKey(p.getUniqueId())){
                UUID uuid = snowmanCreators.get(p.getUniqueId());
                if(snowman.getUniqueId().equals(uuid)){
                    e.setCancelled(true);
                    p.sendMessage("Hop");
                }
            }
        }
    }


}
