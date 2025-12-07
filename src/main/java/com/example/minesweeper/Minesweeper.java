package com.example.minesweeper;

import com.example.minesweeper.utils.PlatformLoader;
import com.example.minesweeper.utils.WorldGenerator;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minesweeper extends JavaPlugin {

    private static Minesweeper plugin;

    @Override
    public void onEnable() {
        Minesweeper.setPlugin(this);

        World world = WorldGenerator.generate("world_minesweeper", Environment.NORMAL, WorldType.FLAT, true);
        // PlatformLoader.savePlatform(world, -1, 0);
        // PlatformLoader.loadPlatform(world, 1, 0);
    }

    @Override
    public void onDisable() {
        // Code ici
    }

    public static void setPlugin(Minesweeper plugin) {
        Minesweeper.plugin = plugin;
    }

    public static Minesweeper getPlugin() {
        return plugin;
    }

}
