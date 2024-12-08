package com.notpatch.nsanta.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EntityDeathByEntityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Entity attacker;
    private final Entity victim;

    public EntityDeathByEntityEvent(Entity attacker, Entity victim){
        this.attacker = attacker;
        this.victim = victim;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public Entity getVictim() {
        return victim;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
