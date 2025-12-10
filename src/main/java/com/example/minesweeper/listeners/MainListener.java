package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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

        if (event.getBlock().getType() == Material.REDSTONE_TORCH) {
            if (PlayerJoinMinesweeperListener.isChunkOf(player)) {
                return;
            }
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (victim.getWorld() != minesweeperWorld) {
            return;
        }

        if (!(damager instanceof Player playerDamager)) {
            return;
        }

        if (playerDamager.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        DamageCause cause = event.getCause();

        if (entity.getWorld() != minesweeperWorld) {
            return;
        }

        if (cause == DamageCause.FALL) {
            event.setCancelled(true);
        }

        if (cause == DamageCause.VOID) {
            event.setCancelled(true);

            ChunkInfo chunkInfo = PlayerJoinMinesweeperListener.getChunkInfoByUid(entity.getUniqueId().toString());

            if (chunkInfo == null) {
                entity.remove();
                return;
            }

            int x = chunkInfo.x * 16 + 8;
            int z = chunkInfo.z * 16 + 8;

            entity.setVelocity(new Vector(0, 0, 0));
            entity.teleport(new Location(minesweeperWorld, x, 100, z));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (action.isLeftClick()) {
            event.setCancelled(true);
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

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        event.setCancelled(true);
    }

}
