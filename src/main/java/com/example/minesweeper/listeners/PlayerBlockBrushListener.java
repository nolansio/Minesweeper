package com.example.minesweeper.listeners;

import com.example.minesweeper.Minesweeper;
import com.example.minesweeper.utils.ChunkInfo;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBrushEvent;

public class PlayerBlockBrushListener implements Listener {

    private final World minesweeperWorld = Minesweeper.getMinesweeperWorld();
    private final Material[] terracottaBlocks = {
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

    @EventHandler
    public void onBlockBrush(BlockBrushEvent event) {
        Player player = event.getPlayer();
        Block sand = event.getBlock();

        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        event.setCancelled(true);
        if (!player.isOp() && !ChunkInfo.isChunkOf(player)) {
            return;
        }

        Block sandstone = sand.getRelative(BlockFace.DOWN);
        if (sandstone.getType() == Material.TNT) {
            sandstone.setType(Material.SANDSTONE);
            sand.setType(Material.AIR);

            minesweeperWorld.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
            TNTPrimed tnt = minesweeperWorld.spawn(sand.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(30);
            return;
        }

        recursivity(sand);
    }

    private void recursivity(Block sand) {
        Block sandstone = sand.getRelative(BlockFace.DOWN);

        int tntCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block nextTo = sandstone.getRelative(x, 0, z);

                if (nextTo.getType() == Material.TNT) {
                    tntCount++;
                }
            }
        }

        sand.setType(terracottaBlocks[tntCount]);

        if (tntCount == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if ((x == 0 && z == 0) || (x != 0 && z != 0)) {
                        continue;
                    }

                    Block nextTo = sandstone.getRelative(x, 0, z);
                    Block upTo = nextTo.getRelative(BlockFace.UP);

                    if (upTo.getType() == Material.SUSPICIOUS_SAND) {
                        recursivity(upTo);
                    }
                }
            }
        }
    }

}
