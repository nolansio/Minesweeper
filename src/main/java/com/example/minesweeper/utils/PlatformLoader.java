package com.example.minesweeper.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlatformLoader {

    private static final int chunkSize = 16;
    private static final int startY = 97;
    private static final int endY = 100;

    public static void savePlatform(World world, int chunkX, int chunkZ) {
        File folder = new File("plugins/minesweeper/resources");
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, "platform.yml");
        YamlConfiguration yaml = new YamlConfiguration();

        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                for (int y = startY; y <= endY; y++) {
                    Block b = world.getBlockAt(chunkX * chunkSize + x, y, chunkZ * chunkSize + z);
                    if (b.getType() != Material.AIR) {
                        yaml.set(x + "." + y + "." + z, b.getType().toString());
                    }
                }
            }
        }

        try {
            yaml.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public static void loadPlatform(World world, int chunkX, int chunkZ) {
        File file = new File("plugins/minesweeper/resources/platform.yml");
        if (!file.exists()) return;

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        for (String xKey : yaml.getKeys(false)) {
            for (String yKey : yaml.getConfigurationSection(xKey).getKeys(false)) {
                for (String zKey : yaml.getConfigurationSection(xKey + "." + yKey).getKeys(false)) {

                    int x = Integer.parseInt(xKey);
                    int y = Integer.parseInt(yKey);
                    int z = Integer.parseInt(zKey);

                    Material material = Material.valueOf(yaml.getString(xKey + "." + yKey + "." + zKey));
                    world.getBlockAt(chunkX * chunkSize + x, y, chunkZ * chunkSize + z).setType(material);
                }
            }
        }
    }

    public static void resetPlatform(World world, int chunkX, int chunkZ) {
        int baseX = chunkX * chunkSize;
        int baseZ = chunkZ * chunkSize;

        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                for (int y = startY; y <= endY; y++) {
                    world.getBlockAt(baseX + x, y, baseZ + z).setType(Material.AIR);
                }
            }
        }
    }
}
