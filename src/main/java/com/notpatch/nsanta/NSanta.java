package com.notpatch.nsanta;

import com.github.retrooper.packetevents.PacketEvents;
import com.notpatch.nsanta.command.CommandGui;
import com.notpatch.nsanta.configuration.MenuConfiguration;
import com.notpatch.nsanta.configuration.TreeData;
import com.notpatch.nsanta.event.EventTriggerListener;
import com.notpatch.nsanta.listener.ChunkLoadListener;
import com.notpatch.nsanta.listener.NPCListener;
import com.notpatch.nsanta.listener.SnowmanListener;
import com.notpatch.nsanta.manager.IntegrationManager;
import com.notpatch.nsanta.model.LanguageLoader;
import com.notpatch.nsanta.task.ChristmasLightTask;
import fr.mrmicky.fastinv.FastInvManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class NSanta extends JavaPlugin {

    private static NSanta instance;

    private IntegrationManager integrationManager;

    private ChristmasLightTask christmasLightTask;

    private LanguageLoader languageLoader;

    private NPCSanta npcSanta;


    @Override
    public void onEnable() {
        instance = this;

        FastInvManager.register(this);

        // Config section
        saveDefaultConfig();
        saveConfig();

        // Custom files
        languageLoader = new LanguageLoader(this);

        TreeData.setup();
        TreeData.save();

        MenuConfiguration.setup();
        MenuConfiguration.save();

        // Integrations
        integrationManager = new IntegrationManager();

        doChristmasTree();

        // Listeners

        getServer().getPluginManager().registerEvents(new SnowmanListener(), this);
        getServer().getPluginManager().registerEvents(new EventTriggerListener(), this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkLoadListener(), this);

        // Commands
        getCommand("nsanta").setExecutor(new CommandGui());

        // PacketEvents
        initPacketEvents();

        npcSanta = new NPCSanta();
    }

    @Override
    public void onDisable() {
        if(christmasLightTask != null){
            christmasLightTask.cancel();
        }
        npcSanta.deleteNPC();

        ChunkLoadListener.removeBoxBlocks();
    }

    public static NSanta getInstance() {
        return instance;
    }

    public IntegrationManager getIntegrationManager() {
        return integrationManager;
    }

    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }

    private void doChristmasTree(){
        if(getConfig().getBoolean("christmas-tree.enabled")){
            if(getConfig().getBoolean("christmas-tree.light-settings.enabled")){
                christmasLightTask = new ChristmasLightTask(this);
                christmasLightTask.runTaskTimer(this, 0, 20L);
            }
        }
    }

    private void initPacketEvents(){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();
    }


    public NPCSanta getNPCSanta() {
        return npcSanta;
    }


}
