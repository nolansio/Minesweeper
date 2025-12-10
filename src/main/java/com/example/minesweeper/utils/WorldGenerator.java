package com.example.minesweeper.utils;

import com.example.minesweeper.Minesweeper;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;

public class WorldGenerator extends ChunkGenerator {

    public static World generate(String name, Environment environment, WorldType worldType, boolean isEmptyWorld) {
        if (Minesweeper.getPlugin().getServer().getWorld(name) != null) {
            return null;
        }

        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.environment(environment);
        worldCreator.type(worldType);

        if (isEmptyWorld) {
            worldCreator.generatorSettings("{\"layers\":[{\"height\":1,\"block\":\"minecraft:air\"}],\"biome\":\"minecraft:plains\"}");
            worldCreator.generateStructures(false);
        }

        World world = worldCreator.createWorld();
        world.setSpawnLocation(0, 100, 0);

        return world;
    }

}
