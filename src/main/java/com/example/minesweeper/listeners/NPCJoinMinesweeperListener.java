package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import com.example.minesweeper.utils.PlatformLoader;
import com.example.minesweeper.utils.SpiralGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class NPCJoinMinesweeperListener implements Listener {

    private static final List<ChunkInfo> occupiedChunks = Collections.synchronizedList(PlayerJoinMinesweeperListener.getOccupiedChunks());
    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() != EntityType.MANNEQUIN) {
            return;
        }

        if (entity.getWorld() == minesweeperWorld) {
            entityJoinMinesweeper((Mannequin) entity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.MANNEQUIN) {
            return;
        }

        if (entity.getWorld() == minesweeperWorld) {
            entityQuitMinesweeper((Mannequin) entity);
        }
    }

    private void entityJoinMinesweeper(Mannequin entity) {
        entity.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.BRUSH));
        entity.getEquipment().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.REDSTONE_TORCH, 10));

        int[] chunkPosition = getFreeChunkPosition();
        if (chunkPosition == null) {
            entity.remove();
            return;
        }

        occupiedChunks.add(new ChunkInfo(chunkPosition[0], chunkPosition[1], entity.getUniqueId().toString()));
        PlatformLoader.loadPlatform(minesweeperWorld, chunkPosition[0], chunkPosition[1]);

        int x = chunkPosition[0] * 16 + 8;
        int z = chunkPosition[1] * 16 + 8;
        entity.teleport(new Location(minesweeperWorld, x, 100, z));

        Bukkit.getScheduler().runTaskLater(Minesweeper.getPlugin(), () -> {
            entity.teleport(new Location(minesweeperWorld, x, 100, z));
        }, 1L);
    }

    private void entityQuitMinesweeper(Mannequin entity) {
        ChunkInfo target = null;

        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.playerName.equals(entity.getUniqueId().toString())) {
                target = chunkInfo;
                break;
            }
        }

        if (target != null) {
            occupiedChunks.remove(target);
            PlatformLoader.resetPlatform(minesweeperWorld, target.x, target.z);
        }
    }

    private int[] getFreeChunkPosition() {
        SpiralGenerator spiral = new SpiralGenerator();
        int[] chunkPosition = null;

        for (int i=0; i < 10000; i++) {
            if (i == 0) {
                chunkPosition = new int[] {0, 0};
            } else {
                chunkPosition = spiral.next();
            }

            if (!isOccupied(chunkPosition)) {
                break;
            }
        }

        return chunkPosition;
    }

    private boolean isOccupied(int[] position) {
        for (ChunkInfo occupiedChunk : occupiedChunks) {
            if (occupiedChunk.x == position[0] && occupiedChunk.z == position[1]) {
                return true;
            }
        }

        return false;
    }

}
