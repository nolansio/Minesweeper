package com.example.minesweeper.utils;

import com.example.minesweeper.Minesweeper;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ResourcePackLoader {

    private static final ConcurrentHashMap<Player, UUID> activePacks = new ConcurrentHashMap<>();

    public static void load(Player player) {
        UUID id = UUID.randomUUID();
        activePacks.put(player, id);

        String url = "https://nolansio.github.io/Minesweeper_Resources/" + Minesweeper.getVersion() + "/minesweeper_pack.zip";
        player.setResourcePack(id, url, null, null, false);
    }

    public static void unload(Player player) {
        UUID id = activePacks.remove(player);

        if (id != null) {
            player.removeResourcePack(id);
        }
    }

}

