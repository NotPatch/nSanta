package com.notpatch.nsanta.event;

import com.notpatch.nsanta.NSanta;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventTriggerListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        Entity victim = e.getEntity();
        if(victim instanceof LivingEntity){
            double damage = e.getDamage();
            double health = ((LivingEntity) victim).getHealth();
            if(health-damage <= 0){
                EntityDeathByEntityEvent event = new EntityDeathByEntityEvent(e.getDamager(), victim);
                NSanta.getInstance().getServer().getPluginManager().callEvent(event);
            }
        }
    }

}
