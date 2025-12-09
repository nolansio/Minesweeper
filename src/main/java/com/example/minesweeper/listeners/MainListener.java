package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainListener implements Listener {

    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getWorld() != minesweeperWorld) {
            return;
        }

        if (entity.getType() == EntityType.PLAYER) {
            Player player = (Player) entity;
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!PlayerJoinMinesweeperListener.isChunkOf(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!PlayerJoinMinesweeperListener.isChunkOf(player)) {
            event.setCancelled(true);
        }
    }

}
