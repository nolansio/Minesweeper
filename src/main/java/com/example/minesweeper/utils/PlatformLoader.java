package com.example.minesweeper.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlatformLoader {

    public static void savePlatform(World world, int chunkX, int chunkZ) {
        File folder = new File("plugins/minesweeper/resources");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, "platform.yml");
        YamlConfiguration yaml = new YamlConfiguration();

        int startY = 97;
        int endY = 100;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = startY; y <= endY; y++) {
                    Block block = world.getBlockAt(chunkX * 16 + x, y, chunkZ * 16 + z);
                    if (block.getType() != Material.AIR) {
                        String path = x + "." + y + "." + z;
                        yaml.set(path, block.getType().toString());
                    }
                }
            }
        }

        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadPlatform(World world, int chunkX, int chunkZ) {
        File file = new File("plugins/minesweeper/resources/platform.yml");
        if (!file.exists()) return;

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        int chunkSize = 16;

        for (String xKey : yaml.getKeys(false)) {
            for (String yKey : yaml.getConfigurationSection(xKey).getKeys(false)) {
                for (String zKey : yaml.getConfigurationSection(xKey + "." + yKey).getKeys(false)) {
                    int x = Integer.parseInt(xKey);
                    int y = Integer.parseInt(yKey);
                    int z = Integer.parseInt(zKey);
                    Material mat = Material.valueOf(yaml.getString(xKey + "." + yKey + "." + zKey));
                    world.getBlockAt(chunkX * chunkSize + x, y, chunkZ * chunkSize + z).setType(mat);
                }
            }
        }
    }

}
