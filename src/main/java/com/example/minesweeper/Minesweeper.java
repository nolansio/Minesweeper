package com.example.minesweeper;

import com.example.minesweeper.utils.WorldGenerator;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minesweeper extends JavaPlugin {

    private static Minesweeper plugin;

    @Override
    public void onEnable() {
        Minesweeper.setPlugin(this);

        WorldGenerator.generate("world_minesweeper", Environment.NORMAL, WorldType.FLAT, true);
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
