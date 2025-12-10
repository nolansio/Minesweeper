package com.example.minesweeper.utils;

import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class ChunkInfo {

    private static final ArrayList<ChunkInfo> occupiedChunks = new ArrayList<>();

    public int x, z;
    public String uid;

    public ChunkInfo(int x, int z, String uid) {
        this.x = x;
        this.z = z;
        this.uid = uid;
    }

    public static ArrayList<ChunkInfo> getOccupiedChunks() {
        return occupiedChunks;
    }

    public static int[] getFreeChunkPosition() {
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

    public static boolean isOccupied(int[] position) {
        for (ChunkInfo occupiedChunk : occupiedChunks) {
            if (occupiedChunk.x == position[0] && occupiedChunk.z == position[1]) {
                return true;
            }
        }

        return false;
    }

    public static ChunkInfo getChunk(String uid) {
        for (ChunkInfo chunkInfo : occupiedChunks) {
            if (chunkInfo.uid.equals(uid)) {
                return chunkInfo;
            }
        }

        return null;
    }

    public static boolean isChunkOf(Entity entity) {
        ChunkInfo chunkInfo = getChunk(entity.getUniqueId().toString());

        int chunkX = entity.getLocation().getBlockX() / 16;
        int chunkZ = entity.getLocation().getBlockZ() / 16;

        return chunkInfo.x == chunkX && chunkInfo.z == chunkZ;
    }

}
