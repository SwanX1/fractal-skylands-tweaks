// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands.provider;

import java.util.List;

import dev.cernavskis.fractalskylandstweaks.util.OpenSimplex2F;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.Point;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.Island;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

public abstract class AbstractIslandProvider {
  public final int maxIslandDistance;
  public final int poissonGenerationStep;

  public AbstractIslandProvider(int maxIslandDistance, int poissonGenerationStep) {
    this.maxIslandDistance = maxIslandDistance;
    this.poissonGenerationStep = poissonGenerationStep;
  }
  
  public long generateIslandSeed(IslandConfiguration island, BlockPos position) {
    return (((long) island.hashCode()) << 32) ^ position.asLong();
  }

  public Island createIsland(IslandConfiguration island, BlockPos position, long seed) {
    BlockPos variantPosition = position.atY(island.altitude + (int) OpenSimplex2F.noise2(seed, position.getX(), position.getY()) * island.altitudeVariance);
    return island.create(variantPosition, generateIslandSeed(island, position) ^ seed);
  }

  protected abstract List<Point<IslandConfiguration>> getNewIslandPlacements(long seed);

  protected abstract boolean shouldExpandPlacementGeneration(ChunkPos pos);

  protected abstract void ensureIslandPlacements(long seed, ChunkAccess chunkAccess);

  public abstract List<Island> getIslands(long seed);
}
