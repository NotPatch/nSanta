package com.notpatch.nsanta.integration;

import com.notpatch.nsanta.NSanta;
import org.bukkit.Bukkit;

public class WorldGuardIntegration{

    private boolean useWorldGuard;

    public WorldGuardIntegration(NSanta main){
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            main.getLogger().severe("Disabling plugin due to missing [WorldGuard]!");
            useWorldGuard = false;
        }
        main.getLogger().info("Hooked into WorldGuard!");
        useWorldGuard = true;
    }

    public boolean isUseWorldGuard() {
        return useWorldGuard;
    }

}
