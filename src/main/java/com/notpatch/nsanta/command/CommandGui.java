package com.notpatch.nsanta.command;

import com.notpatch.nsanta.NPCSanta;
import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.gui.TreeGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandGui implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Player p = (Player) sender;
        new TreeGui().open(p);
        NPCSanta santa = new NPCSanta();
        santa.createSantaNPC(p.getLocation());
        new BukkitRunnable(){
            @Override
            public void run() {
                santa.smoothRotateNPC((float)-177, (float)1, 20*10);
            }
        }.runTaskLater(NSanta.getInstance(), 20*5);


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
