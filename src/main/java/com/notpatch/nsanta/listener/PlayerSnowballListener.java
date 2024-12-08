package com.notpatch.nsanta.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerSnowballListener implements Listener {

    @EventHandler
    public void onSnowballHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Snowball)) return;
        if(!(SnowmanListener.getShooter(e.getDamager()) instanceof Player)) return;
        if(!(e.getEntity() instanceof Player)) return;
        Player attacker = (Player) SnowmanListener.getShooter(e.getDamager());
        Player victim = (Player) e.getEntity();



    }

}
