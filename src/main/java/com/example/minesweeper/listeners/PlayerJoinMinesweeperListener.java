package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import com.example.minesweeper.utils.PlatformLoader;
import com.example.minesweeper.utils.ResourcePackLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class PlayerJoinMinesweeperListener implements Listener {

    private static final List<ChunkInfo> occupiedChunks = Collections.synchronizedList(ChunkInfo.getOccupiedChunks());
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.getWorld() == minesweeperWorld) {
            playerQuitMinesweeper(player);
        }
    }

    private void playerJoinMinesweeper(Player player) {
        ResourcePackLoader.load(player);

        player.getInventory().clear();
        player.getInventory().setItem(0, new ItemStack(Material.BRUSH));
        player.getInventory().setItem(1, new ItemStack(Material.REDSTONE_TORCH, 15));

        int[] chunkPosition = ChunkInfo.getFreeChunkPosition();
        if (chunkPosition == null) {
            player.teleport(Minesweeper.getMainWorld().getSpawnLocation());
            player.sendMessage("There are no more places available, please try again later");
            return;
        }

        occupiedChunks.add(new ChunkInfo(chunkPosition[0], chunkPosition[1], player.getUniqueId().toString()));
        PlatformLoader.load(minesweeperWorld, chunkPosition[0], chunkPosition[1]);

        int x = chunkPosition[0] * 16 + 8;
        int z = chunkPosition[1] * 16 + 8;
        player.teleport(new Location(minesweeperWorld, x, 100, z));
    }

    private void playerQuitMinesweeper(Player player) {
        ResourcePackLoader.unload(player);
        ChunkInfo target = null;

        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.uid.equals(player.getUniqueId().toString())) {
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
