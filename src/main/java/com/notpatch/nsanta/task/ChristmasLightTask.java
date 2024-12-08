package com.notpatch.nsanta.task;

import com.notpatch.nsanta.NSanta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

public class ChristmasLightTask extends BukkitRunnable {

    private final NSanta main;

    private Location treeLocation;

    private Particle lightParticle;
    private int particleAmount;
    private double radius;
    private double rotationSpeed;


    public ChristmasLightTask(NSanta main){
        this.main = main;
        loadElements();
    }

    private void loadElements(){
        Configuration config = main.getConfig();

        treeLocation = new Location(
                Bukkit.getWorld(config.getString("christmas-tree.tree-location.world")),
                config.getDouble("christmas-tree.tree-location.x"),
                config.getDouble("christmas-tree.tree-location.y"),
                config.getDouble("christmas-tree.tree-location.z"));

        lightParticle = Particle.valueOf(config.getString("christmas-tree.light-settings.particle"));
        particleAmount = config.getInt("christmas-tree.light-settings.particle-amount");
        radius = config.getDouble("christmas-tree.light-settings.radius");
        rotationSpeed = config.getDouble("christmas-tree.light-settings.rotation-speed");
    }

    @Override
    public void run() {
        //ty chatgpt
        double angle = 0;

        for (int i = 0; i < 360; i += 90) {
            double radians = Math.toRadians(i + angle);
            double x = radius * Math.cos(radians);
            double z = radius * Math.sin(radians);
            Location particleLocation = treeLocation.clone().add(x, 0, z);

            treeLocation.getWorld().spawnParticle(lightParticle, particleLocation, particleAmount);
            angle += rotationSpeed;
        }
        if (angle >= 360) {
            angle = 0;
        }


    }
}
