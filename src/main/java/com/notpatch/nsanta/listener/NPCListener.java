package com.notpatch.nsanta.listener;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.gui.TreeGui;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCListener implements Listener {

    @EventHandler
    public void onInteract(NPCRightClickEvent e){
        if(e.getNPC().getUniqueId().equals(NSanta.getInstance().getNPCSanta().santaNPC.getUniqueId())){
            Player p = e.getClicker();
            new TreeGui().open(p);
        }
    }

}
