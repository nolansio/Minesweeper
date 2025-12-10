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

    @EventHandler
    public void onBlockBrush(BlockBrushEvent event) {
        Player player = event.getPlayer();
        Block sand = event.getBlock();

        if (player.getWorld() != minesweeperWorld) {
            return;
        }

        event.setCancelled(true);

        boolean isChunkOfPlayer = ChunkInfo.isChunkOf(player);

        if (isChunkOfPlayer) {
            player.sendMessage("Ton chunk");
        } else {
            player.sendMessage("Pas ton chunk");
        }

        if (!player.isOp() && !isChunkOfPlayer) {
            return;
        }

        Block sandstone = sand.getRelative(BlockFace.DOWN);
        if (sandstone.getType() == Material.TNT) {
            sandstone.setType(Material.SANDSTONE);
            sand.setType(Material.AIR);

            minesweeperWorld.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);
            TNTPrimed tnt = minesweeperWorld.spawn(sand.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(40);
            return;
        }

        int tntCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = sandstone.getRelative(x, 0, z);
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

        sand.setType(blocks[tntCount]);
    }

}
