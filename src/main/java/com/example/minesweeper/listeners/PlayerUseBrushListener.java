package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerUseBrushListener implements Listener {

    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();

    @EventHandler
    public void onPlayerUseBrush(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        if (!PlayerJoinMinesweeperListener.isChunkOf(player)) {
            return;
        }

        if (event.getMaterial() != Material.BRUSH) {
            return;
        }

        if (!action.isRightClick()) {
            return;
        }

        Block clicked = event.getClickedBlock();
        if (clicked == null) {
            return;
        }

        if (clicked.getType() != Material.SUSPICIOUS_SAND) {
            return;
        }

        Block below = clicked.getRelative(BlockFace.DOWN);
        if (below.getType() == Material.TNT) {
            below.setType(Material.SAND);
            clicked.setType(Material.AIR);

            minesweeperWorld.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
            minesweeperWorld.spawn(clicked.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            return;
        }

        int tntCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = below.getRelative(x, 0, z);
                if (block.getType() == Material.TNT) {
                    tntCount++;
                }
            }
        }

        Material[] blocks = {
                Material.SAND,
                Material.BLUE_GLAZED_TERRACOTTA,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.PURPLE_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA,
                Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.BLACK_GLAZED_TERRACOTTA,
                Material.GRAY_GLAZED_TERRACOTTA
        };

        int i = Math.min(tntCount, blocks.length - 1);
        clicked.setType(blocks[i]);
    }

}
