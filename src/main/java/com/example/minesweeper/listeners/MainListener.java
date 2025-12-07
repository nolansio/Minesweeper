package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.SpiralGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;

public class MainListener implements Listener {

    private static final ArrayList<ChunkInfo> occupiedChunks = new ArrayList<>();

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld() != Minesweeper.getMinesweeperWorld()) {
            return;
        }

        int[] chunkPosition = getFreeChunkPosition();
        if (chunkPosition == null) {
            player.teleport(Minesweeper.getMainWorld().getSpawnLocation());
            player.sendMessage(Component.text("<red>There are no more places available, please try again later</red>"));
            return;
        }

        occupiedChunks.add(new ChunkInfo(chunkPosition[0], chunkPosition[1], player.getName()));
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

    public static class ChunkInfo {
        public int x, z;
        public String playerName;

        public ChunkInfo(int x, int z, String playerName) {
            this.x = x;
            this.z = z;
            this.playerName = playerName;
        }
    }

}
