// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.LevelData;

public final class WorldgenUtil {
  private WorldgenUtil() {
  } // Uninstantiable

  public static BlockPos getWorldSpawn(LevelData level) {
    return new BlockPos(level.getXSpawn(), level.getYSpawn(), level.getZSpawn());
  }

  public static boolean isChunkSpawn(ChunkPos chunk, LevelData level) {
    BlockPos spawn = WorldgenUtil.getWorldSpawn(level);
    return chunk.getMinBlockX() <= spawn.getX() && spawn.getX() <= chunk.getMaxBlockX() &&
        chunk.getMinBlockZ() <= spawn.getZ() && spawn.getZ() <= chunk.getMaxBlockZ();
  }

  public static boolean isWithinChunk(ChunkPos chunk, BlockPos pos) {
    return chunk.getMinBlockX() <= pos.getX() && pos.getX() <= chunk.getMaxBlockX() &&
        chunk.getMinBlockZ() <= pos.getZ() && pos.getZ() <= chunk.getMaxBlockZ();
  }

  public static boolean isWithinChunk(ChunkPos chunk, BlockPos pos, int leeway) {
    return chunk.getMinBlockX() - leeway <= pos.getX() && pos.getX() <= chunk.getMaxBlockX() + leeway &&
        chunk.getMinBlockZ() - leeway <= pos.getZ() && pos.getZ() <= chunk.getMaxBlockZ() + leeway;
  }

  public static boolean isWithinBoundary(ChunkPos chunk, int boundary) {
    int halfBoundary = boundary / 2;
    return chunk.getMinBlockX() >= -halfBoundary && chunk.getMinBlockZ() >= -halfBoundary &&
        chunk.getMaxBlockX() <= halfBoundary && chunk.getMaxBlockZ() <= halfBoundary;
  }
}
