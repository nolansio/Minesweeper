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

1. Download the plugin `.jar` file corresponding to your version
2. Place it in your Minecraft server's `plugins/` folder
3. Open the `.jar` archive and extract:
    - `minesweeper_pack/` (resource pack folder)
    - `platform.yml` (configuration file)
4. Create the `plugins/minesweeper/resources/` folder (if it doesn't exist)
5. Place `platform.yml` in `plugins/minesweeper/resources/`
6. Zip the extracted `minesweeper_pack/` folder into `minesweeper_pack.zip` and install it on your server
7. Restart the server
8. The `world_minesweeper` world will be automatically created on startup

> **Important**: The resource pack is essential for correct number display!

## 🎨 Block System

The plugin uses glazed terracotta blocks to represent the number of adjacent mines.
A **resource pack** is provided with the plugin to retexture these blocks with classic minesweeper numbers:

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

## 👨‍💻 Plugin Architecture

The plugin is structured in a modular way:

```
com.example.minesweeper/
├── Minesweeper.java              # Main class
├── listeners/                     # Event handlers
│   ├── MainListener.java         # General protections
│   ├── PlayerJoinMinesweeperListener.java
│   ├── NPCJoinMinesweeperListener.java
│   └── PlayerBlockBrushListener.java  # Minesweeper logic
└── utils/                         # Utility classes
    ├── ChunkInfo.java            # Chunk management
    ├── PlatformLoader.java       # Platform loading
    ├── WorldGenerator.java       # World generation
    └── SpiralGenerator.java      # Spiral allocation
```

### Key Components

- **MainListener**: Manages all protections (breaking/placing blocks, damage, items)
- **PlayerJoinMinesweeperListener**: Assigns platforms to entering/leaving players
- **NPCJoinMinesweeperListener**: Manages mannequin lifecycle
- **PlayerBlockBrushListener**: Implements minesweeper logic (reveal, recursion, explosion)
- **ChunkInfo**: Tracks chunk occupancy (who occupies which chunk)
- **PlatformLoader**: Loads and resets game platforms
- **SpiralGenerator**: Assigns chunks in a spiral pattern to optimize space

## 🔍 Technical Details

- **Platform Assignment**: Players are placed in a spiral around spawn to optimize space usage
- **Chunk Isolation**: Each platform occupies a complete chunk (16×16 blocks)
- **Automatic Clean-up**: When a player leaves, their chunk is reset and becomes available again
- **Safe Teleportation**: Players are teleported to Y=100 at the center of their chunk
- **Capacity Management**: If all chunks are occupied (10000), new players are sent back to the main world

## 📄 License

This project is licensed under the MIT License. See the **LICENSE** file for more details.
