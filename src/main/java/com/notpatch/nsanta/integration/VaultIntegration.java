package com.notpatch.nsanta.integration;

import com.notpatch.nsanta.NSanta;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegration {

    private boolean useVault;

    public VaultIntegration() {
        if(!setupEconomy()){
            NSanta.getInstance().getLogger().warning("Vault not found, integration disabled!");
            useVault = false;
        }
        NSanta.getInstance().getLogger().info("Hooked into Vault!");
        useVault = true;
    }

    private static Economy econ = null;

    private boolean setupEconomy() {
        if (NSanta.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = NSanta.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    public boolean isUseVault() {
        return useVault;
    }

}
