package com.example.minesweeper;

import com.example.minesweeper.listeners.*;
import com.example.minesweeper.utils.WorldGenerator;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minesweeper extends JavaPlugin {

    private static Minesweeper plugin;

    @Override
    public void onEnable() {
        Minesweeper.setPlugin(this);

        WorldGenerator.generate("world_minesweeper", Environment.NORMAL, WorldType.FLAT, true);
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinMinesweeperListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCJoinMinesweeperListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerBlockBrushListener(), this);
    }

    @Override
    public void onDisable() {
        World world = getMinesweeperWorld();
        world.getEntities().forEach(entity -> {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        });
    }

    public static void setPlugin(Minesweeper plugin) {
        Minesweeper.plugin = plugin;
    }

    public static Minesweeper getPlugin() {
        return plugin;
    }

    public static World getMainWorld() {
        return plugin.getServer().getWorld("world");
    }

    public static World getMinesweeperWorld() {
        return plugin.getServer().getWorld("world_minesweeper");
    }

    public static String getVersion() {
        return plugin.getServer().getBukkitVersion().split("-")[0];
    }

    public static String getServerType() {
        return plugin.getServer().getName();
    }

}
