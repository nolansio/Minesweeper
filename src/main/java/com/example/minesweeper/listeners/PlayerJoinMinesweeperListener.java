package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import com.example.minesweeper.utils.PlatformLoader;
import com.example.minesweeper.utils.SpiralGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerJoinMinesweeperListener implements Listener {

    private static final ArrayList<ChunkInfo> occupiedChunks = new ArrayList<>();
    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld() == minesweeperWorld) {
            playerJoinMinesweeper(player);
        }

        if (event.getFrom() == minesweeperWorld) {
            playerQuitMinesweeper(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld() == minesweeperWorld) {
            playerJoinMinesweeper(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld() == minesweeperWorld) {
            playerQuitMinesweeper(player);
        }
    }

    private void playerJoinMinesweeper(Player player) {
        player.getInventory().clear();
        player.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.BRUSH));
        player.getEquipment().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.REDSTONE_TORCH, 10));

        int[] chunkPosition = getFreeChunkPosition();
        if (chunkPosition == null) {
            player.teleport(Minesweeper.getMainWorld().getSpawnLocation());
            player.sendMessage(Component.text("<red>There are no more places available, please try again later</red>"));
            return;
        }

        occupiedChunks.add(new ChunkInfo(chunkPosition[0], chunkPosition[1], player.getUniqueId().toString()));
        PlatformLoader.loadPlatform(minesweeperWorld, chunkPosition[0], chunkPosition[1]);

        int x = chunkPosition[0] * 16 + 8;
        int z = chunkPosition[1] * 16 + 8;
        player.teleport(new Location(minesweeperWorld, x, 100, z));
    }

    private void playerQuitMinesweeper(Player player) {
        ChunkInfo target = null;

        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.UID.equals(player.getUniqueId().toString())) {
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

    public static ArrayList<ChunkInfo> getOccupiedChunks() {
        return occupiedChunks;
    }

    public static ChunkInfo getChunkInfoByName(String playerName) {
        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.UID.equals(playerName)) {
                return chunkInfo;
            }
        }

        return null;
    }

    public static boolean isChunkOf(LivingEntity entity) {
        int chunkX = entity.getLocation().getBlockX() / 16;
        int chunkZ = entity.getLocation().getBlockZ() / 16;
        String uid = entity.getUniqueId().toString();

        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.UID.equals(uid)) {
                return chunkInfo.x == chunkX && chunkInfo.z == chunkZ;
            }
        }

        return false;
    }

}
