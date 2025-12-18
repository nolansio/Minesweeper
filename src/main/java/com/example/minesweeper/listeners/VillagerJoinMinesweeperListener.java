package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import com.example.minesweeper.utils.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class VillagerJoinMinesweeperListener implements Listener {

    private static final List<ChunkInfo> occupiedChunks = Collections.synchronizedList(ChunkInfo.getOccupiedChunks());
    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }

        if (entity.getWorld() == minesweeperWorld) {
            entityJoinMinesweeper((Villager) entity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }

        if (entity.getWorld() == minesweeperWorld) {
            entityQuitMinesweeper((Villager) entity);
        }
    }

    private void entityJoinMinesweeper(Villager entity) {
        entity.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.BRUSH));

        int[] chunkPosition = ChunkInfo.getFreeChunkPosition();
        if (chunkPosition == null) {
            entity.remove();
            return;
        }

        occupiedChunks.add(new ChunkInfo(chunkPosition[0], chunkPosition[1], entity.getUniqueId().toString()));
        PlatformLoader.load(minesweeperWorld, chunkPosition[0], chunkPosition[1]);

        int x = chunkPosition[0] * 16 + 8;
        int z = chunkPosition[1] * 16 + 8;
        entity.teleport(new Location(minesweeperWorld, x, 100, z));

        Bukkit.getScheduler().runTaskLater(Minesweeper.getPlugin(), () -> {
            entity.teleport(new Location(minesweeperWorld, x, 100, z));
        }, 1L);
    }

    private void entityQuitMinesweeper(Villager entity) {
        ChunkInfo target = null;

        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.uid.equals(entity.getUniqueId().toString())) {
                target = chunkInfo;
                break;
            }
        }

        if (target != null) {
            occupiedChunks.remove(target);
            PlatformLoader.unload(minesweeperWorld, target.x, target.z);
        }
    }

}
