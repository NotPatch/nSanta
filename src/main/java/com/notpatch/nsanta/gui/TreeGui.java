package com.notpatch.nsanta.gui;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.configuration.MenuConfiguration;
import com.notpatch.nsanta.configuration.TreeData;
import com.notpatch.nsanta.util.StringUtil;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeGui extends FastInv implements Listener {

    private List<ItemStack> requiredItems = new ArrayList<>();
    private List<Material> requiredMaterials = new ArrayList<>();


    public TreeGui() {
        super(54, StringUtil.hexColor(MenuConfiguration.get().getString("title")));

        for(String line : NSanta.getInstance().getConfig().getStringList("christmas-tree.ingredients")){
            String[] split = line.split(":");
            int current = TreeData.get().getInt("treedata." + split[0].toUpperCase());
            int max = Integer.parseInt(split[1]);
            int finalAmount = max - current;
            ItemStack item = new ItemBuilder(Material.valueOf(split[0])).amount(1)
                    .name(StringUtil.hexColor(MenuConfiguration.get().getString("ingredients.name").replace("%integredient%", split[0].toUpperCase())))
                    .lore(StringUtil.getColoredList(MenuConfiguration.get().getStringList("ingredients.lore"), "%amount%", String.valueOf(finalAmount)))
                    .build();
            requiredItems.add(item);
            requiredMaterials.add(item.getType());

        }
        int itemIndex = 0;
        List<Integer> fillerSlots = MenuConfiguration.get().getIntegerList("filler-item.slots");
        Collections.sort(fillerSlots);

        for (int i = 0; i < 54; i++) {
            if (fillerSlots.contains(i)) {
                setItem(i, new ItemBuilder(Material.valueOf(MenuConfiguration.get().getString("filler-item.material"))).name(" ").build());
            }
        }

        for (int i = 0; i < 54; i++) {
            if (!fillerSlots.contains(i) && itemIndex < requiredItems.size()) {
                setItem(i, requiredItems.get(itemIndex));
                itemIndex++;
            }
        }

    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() instanceof PlayerInventory){
            ItemStack item = e.getCurrentItem();
            Player p = (Player) e.getWhoClicked();
            doProcess(p, item, e);
        }
    }

    public static void doProcess(Player p, ItemStack item, Cancellable e){
        for(String line : NSanta.getInstance().getConfig().getStringList("christmas-tree.ingredients")){
            String[] split = line.split(":");
            if(split[0].equalsIgnoreCase(item.getType().name())){
                e.setCancelled(true);

                int itemAmount = item.getAmount();
                int current = TreeData.get().getInt("treedata." + split[0].toUpperCase());
                int max = Integer.parseInt(split[1]);
                int finalAmount = max - current;

                if(split[0].equalsIgnoreCase(item.getType().name())){
                    if(current < max){
                        if(itemAmount > finalAmount){
                            item.setAmount(itemAmount - finalAmount);
                            TreeData.get().set("treedata." + split[0].toUpperCase(), max);
                            TreeData.save();
                            p.closeInventory();
                            NSanta.getInstance().getNPCSanta().smoothRotateNPC(20*10);
                            Bukkit.broadcastMessage(NSanta.getInstance().getLanguageLoader().get("tree-integredient-added").replace("%player%", p.getName()).replace("%integredient%", split[0].toUpperCase()).replace("%amount%", String.valueOf(finalAmount)));

                        }else{
                            item.setAmount(0);
                            TreeData.get().set("treedata." + split[0].toUpperCase(), current + itemAmount);
                            TreeData.save();
                            p.closeInventory();
                            NSanta.getInstance().getNPCSanta().smoothRotateNPC(20*10);

                        }
                    }
                }

            }

        }
    }


}
