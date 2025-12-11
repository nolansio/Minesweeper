# 🎮 Minesweeper - Minecraft Plugin

A Minecraft plugin that transforms the classic Minesweeper game into an immersive multiplayer experience!

## 📋 Description

This plugin creates a dedicated world where players can play Minesweeper in Minecraft.
Each player or NPC receives their own isolated game platform with a randomly generated 10×10 minesweeper grid.

## ✨ Features

- **Dedicated World**: Automatic creation of an empty `world_minesweeper` world for the game
- **Individual Platforms**: Each player gets their own chunk dynamically generated upon arrival
- **Minesweeper Mechanics**:
    - Use the **brush** to reveal tiles
    - Glazed terracotta blocks are retextured via a resource pack to display minesweeper numbers
    - Uncover a TNT and the entire platform explodes!
- **Mannequin Support**: Fake players (mannequins) can be spawned for testing purposes
- **Automatic Management**: Platforms are created on arrival and cleaned up on departure
- **Complete Protection**: Players can only interact with their own chunk

## 🎯 How to Play

1. Join the `world_minesweeper` world:
   ```
   /execute in minecraft:world_minesweeper run tp <player> 0 100 0
   ```
2. You automatically receive:
    - A **brush** to reveal tiles
    - **15 redstone torches** to mark suspected TNTs
3. Use the brush on suspicious sand to reveal what's underneath
4. Glazed terracotta blocks are retextured to visually display the number of adjacent mines:
    - Sand: No mines (empty tile)
    - Blue glazed terracotta: 1 mine
    - Green glazed terracotta: 2 mines
    - Red glazed terracotta: 3 mines
    - etc.
5. Avoid uncovering TNT, or it's guaranteed explosion!

## 🔧 Installation

1. Download the plugin `.jar` file for your server version.
2. Place it in the `plugins/` folder of your Minecraft server.
3. Restart the server — that’s it!

> **Important**:
> - The empty `world_minesweeper` world is automatically created when the server starts.
> - The **resource pack** is automatically loaded for players when they join the `world_minesweeper` world, fetched from an external link.
> - The **platform configuration** is automatically downloaded to the `plugins/minesweeper/resources/` folder from an external link.

## 🎨 Block System

The plugin uses glazed terracotta blocks to represent the number of adjacent mines.
A **resource pack** is provided to retexture these blocks with classic minesweeper numbers:

| Number of Mines | Block                        |
|-----------------|------------------------------|
| 0               | Sand                         |
| 1               | Blue Glazed Terracotta       |
| 2               | Green Glazed Terracotta      |
| 3               | Red Glazed Terracotta        |
| 4               | Purple Glazed Terracotta     |
| 5               | Yellow Glazed Terracotta     |
| 6               | Light Blue Glazed Terracotta |
| 7               | Cyan Glazed Terracotta       |
| 8               | Black Glazed Terracotta      |
| 9               | Gray Glazed Terracotta       |

## 🛡️ Protections

In the `world_minesweeper` world, the following protections are active:

- ❌ **Breaking Blocks**: Impossible (except redstone torches on your own platform)
- ❌ **Placing Blocks**: Impossible (except redstone torches on your own platform)
- ❌ **Dropping Items**: Blocked to prevent item loss
- ❌ **Attacking Entities**: Impossible between players and NPCs
- ✅ **Fall Damage**: Disabled throughout the world
- 🔄 **Void Fall**: Automatic teleportation back to your platform

> **Note**: Only server operators can bypass these restrictions.

## 🎮 Advanced Features

### Automatic Reveal
When you reveal a tile with no adjacent mines (sand),
all neighboring tiles are automatically revealed recursively,
just like in classic minesweeper!

### Mannequin Management
Mannequins (NPCs) can join the world to test the plugin. They automatically receive:
- A brush
- Their own game platform
- The same restrictions as players

## 🔍 Technical Details

- **Platform Assignment**: Players are placed in a spiral around spawn to optimize space usage
- **Chunk Isolation**: Each platform occupies a complete chunk (16×16 blocks)
- **Automatic Clean-up**: When a player leaves, their chunk is reset and becomes available again
- **Safe Teleportation**: Players are teleported to Y=100 at the center of their chunk
- **Capacity Management**: If all chunks are occupied (10000), new players are sent back to the main world

## 📄 License

This project is licensed under the MIT License. See the **LICENSE** file for more details.
