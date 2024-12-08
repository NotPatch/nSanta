package com.notpatch.nsanta.manager;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.integration.VaultIntegration;
import com.notpatch.nsanta.integration.WorldGuardIntegration;

public class IntegrationManager {

    private VaultIntegration vaultIntegration;
    private WorldGuardIntegration worldGuardIntegration;

    public IntegrationManager() {
        setup();
    }

    private void setup(){
        vaultIntegration = new VaultIntegration();
        worldGuardIntegration = new WorldGuardIntegration(NSanta.getInstance());
    }

    public VaultIntegration getVaultIntegration() {
        return vaultIntegration;
    }

    public WorldGuardIntegration getWorldGuardIntegration() {
        return worldGuardIntegration;
    }

}
