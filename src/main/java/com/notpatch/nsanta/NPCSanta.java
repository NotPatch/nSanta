package com.notpatch.nsanta;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCSanta {

    private NPC santaNPC = getOrCreateSantaNPC();

    public void createSantaNPC(Location location) {
        if (santaNPC == null) {
            santaNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Santa Claus");
        }

        if (!santaNPC.isSpawned()) {
            santaNPC.spawn(location);
        } else {
            santaNPC.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    private void sendNPCAnimation() {
        WrapperPlayServerEntityAnimation animationPacket = new WrapperPlayServerEntityAnimation(
                santaNPC.getEntity().getEntityId(),
                WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
        );
        Bukkit.getOnlinePlayers().forEach(player ->
                PacketEvents.getAPI().getPlayerManager().sendPacket(player, animationPacket)
        );
    }

    public void smoothRotateNPC(float targetYaw, float targetPitch, long duration) {
        Location initialLocation = santaNPC.getStoredLocation();

        if (!santaNPC.isSpawned()) return;

        float initialYaw = initialLocation.getYaw();
        float initialPitch = initialLocation.getPitch();

        rotateToYawPitch(targetYaw, targetPitch, duration, () -> {
            rotateToYawPitch(initialYaw, initialPitch, duration, null);
        });
    }

    private void rotateToYawPitch(float targetYaw, float targetPitch, long duration, Runnable onComplete) {
        int steps = 20;
        long interval = duration / steps;

        float currentYaw = santaNPC.getStoredLocation().getYaw();
        float currentPitch = santaNPC.getStoredLocation().getPitch();

        float yawStep = (targetYaw - currentYaw) / steps;
        float pitchStep = (targetPitch - currentPitch) / steps;

        for (int i = 0; i <= steps; i++) {
            final int step = i;
            Bukkit.getScheduler().runTaskLater(NSanta.getInstance(), () -> {
                if (!santaNPC.isSpawned()) return;

                float newYaw = currentYaw + (yawStep * step);
                float newPitch = currentPitch + (pitchStep * step);

                Location loc = santaNPC.getEntity().getLocation();
                loc.setYaw(newYaw);
                loc.setPitch(newPitch);
                santaNPC.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

                if (step == steps && onComplete != null) {
                    sendNPCAnimation();
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            onComplete.run();
                        }
                    }.runTaskLater(NSanta.getInstance(), 20*2);

                }
            }, step * interval / 50L);
        }
    }


    private NPC getOrCreateSantaNPC() {
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if (npc.getName().equals("Santa Claus")) {
                return npc;
            }
        }

        NPC newSantaNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Santa Claus");
        newSantaNPC.getOrAddTrait(SkinTrait.class).setSkinName("faat");
        return newSantaNPC;
    }

}
