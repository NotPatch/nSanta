package com.notpatch.nsanta.listener;

import com.notpatch.nsanta.NSanta;
import com.notpatch.nsanta.util.ItemUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChunkLoadListener implements Listener {

    private final Random random = new Random();
    public static List<Location> boxLocations = new ArrayList<>();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        World world = chunk.getWorld();

        //if(!world.getName().equals("xd")) return;
        if(!e.getChunk().isEntitiesLoaded()) return;
        Entity[] entities = chunk.getEntities();
        List<Entity> entityList = Arrays.stream(entities).toList();
        if(entityList.stream().noneMatch(entity -> entity.getType() != EntityType.PLAYER)) return;

        //int size = e.getChunk().getPlayersSeeingChunk().size();
        //if(size < 1) return;

        if(random.nextInt(10) == 2){
            int count = random.nextInt(2) + 1;
            for(int i = 0; i < count; i++) {

                int x = chunk.getX() * 16 + random.nextInt(16);
                int z = chunk.getZ() * 16 + random.nextInt(16);

                Block highestBlockAt = world.getHighestBlockAt(x, z);
                if(isNearbyBox(highestBlockAt.getLocation())) return;

                while ((highestBlockAt.getType() == Material.AIR || !highestBlockAt.getType().isSolid()) && highestBlockAt.getY() > 0) {
                    highestBlockAt = highestBlockAt.getRelative(0,-1,0);
                }

                Block finalBlock = highestBlockAt.getRelative(0,1,0);
                finalBlock.setType(Material.PLAYER_HEAD);

                if(world.getBlockAt(finalBlock.getLocation()).getState() instanceof Skull skull){
                    skull.setOwnerProfile(ItemUtil.crateRandomProfile(NSanta.getInstance().getConfig().getString("christmas-box.texture")));
                    skull.update(true);
                }
                boxLocations.add(finalBlock.getLocation());
                Bukkit.broadcastMessage("Generated a Christmas Box at: " + finalBlock.getLocation().getX() + ", " + finalBlock.getLocation().getY() + ", " + finalBlock.getLocation().getZ());
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() == null) return;

        if(boxLocations.contains(e.getClickedBlock().getLocation())){
            e.getClickedBlock().setType(Material.AIR);
            boxLocations.remove(e.getClickedBlock().getLocation());
            Inventory inventory = Bukkit.createInventory(null, 27, "Christmas Box");
            ItemUtil.fillChest(inventory, ItemUtil.getItemsFromList(NSanta.getInstance().getConfig().getStringList("christmas-box.rewards")));
            e.getPlayer().openInventory(inventory);
        }
    }

    public static void removeBoxBlocks(){
        for(Location location : boxLocations){
            location.getBlock().setType(Material.AIR);
            NSanta.getInstance().getLogger().info("Removed a Christmas Box at: " + location.getX() + ", " + location.getY() + ", " + location.getZ());
        }
    }

    private boolean isNearbyBox(Location location){
        for(Location loc : boxLocations){
            if(loc.distance(location) < 20){
                return true;
            }
        }
        return false;
    }

}
