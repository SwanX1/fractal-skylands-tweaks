// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands.provider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import dev.cernavskis.fractalskylandstweaks.core.FSTIslandConfigurations;
import dev.cernavskis.fractalskylandstweaks.util.WorldgenUtil;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.Point;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.PointConfiguration;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.PoissonUtil;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.Island;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class IslandProvider extends AbstractIslandProvider {
  private static final List<PointConfiguration<IslandConfiguration>> WEIGHTED_ISLAND_CONFIGS = new ArrayList<>();
  private static void addIslandConfig(IslandConfiguration config) {
    WEIGHTED_ISLAND_CONFIGS.add(new PointConfiguration<>(config.minDistance, config.weight, config));
  }
  
  static {
    for (Field field : FSTIslandConfigurations.class.getDeclaredFields()) {
      if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && field.getType().equals(IslandConfiguration.class)) {
        try {
          addIslandConfig((IslandConfiguration) field.get(null));
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static int largestConfiguredRadius = Integer.MIN_VALUE;
  private static void ensureLargestConfiguredRadius() {
    // We need to ensure that we're not creating new islands which could generate inside our boundaries,
    // in which case the already generated chunks, which should contain that island, will not, and we'll have a chunk border
    if (IslandProvider.largestConfiguredRadius == Integer.MIN_VALUE) {
      IslandProvider.largestConfiguredRadius = WEIGHTED_ISLAND_CONFIGS.stream()
        .map(pointConfig -> pointConfig.data)
        .mapToInt(IslandConfiguration::getMaxPossibleRadius)
        .max().getAsInt();
    }
  }
  
  public ArrayList<Point<IslandConfiguration>> generatedIslandPositions = new ArrayList<>(0);
  private List<Island> cachedIslands = new ArrayList<>();

  private int currentGenerationSize;
  private int lastGenerationSize = 0;

  public IslandProvider(int maxIslandDistance, int poissonGenerationStep) {
    super(maxIslandDistance, poissonGenerationStep);

    // We need to ensure that we can actually generate an island at the start
    // it's either the poisson generation step or the largest minimum distance
    this.currentGenerationSize = Math.max(
      this.poissonGenerationStep,
      WEIGHTED_ISLAND_CONFIGS.stream()
        .max((a, b) -> Integer.compare(a.data.minDistance, b.data.minDistance)).get().data.minDistance
    );
  }

  protected List<Point<IslandConfiguration>> getNewIslandPlacements(long seed) {
    if (this.lastGenerationSize != this.currentGenerationSize) {
      this.lastGenerationSize = this.currentGenerationSize;
      
      RandomSource random = new XoroshiroRandomSource(seed);
  
      List<Point<IslandConfiguration>> points = PoissonUtil.generatePoints(currentGenerationSize, currentGenerationSize, 48, random, WEIGHTED_ISLAND_CONFIGS, this.generatedIslandPositions);
  
      // Preemptively increase the cache size to add new points
      this.generatedIslandPositions.ensureCapacity(this.generatedIslandPositions.size() + points.size());
      this.generatedIslandPositions.addAll(points);

      return points;
    }

    return Collections.emptyList();
  }

  // Used in IslandChunkGenerator to add the starting island
  public void addIslandPlacement(Point<IslandConfiguration> startIslandPoint) {
    this.generatedIslandPositions.add(startIslandPoint);
  }

  protected boolean shouldExpandPlacementGeneration(ChunkPos pos) {
    return !WorldgenUtil.isWithinBoundary(pos, (int) (this.currentGenerationSize - largestConfiguredRadius));
  }

  public void ensureIslandPlacements(long seed, ChunkAccess chunkAccess) {
    IslandProvider.ensureLargestConfiguredRadius();

    while (shouldExpandPlacementGeneration(chunkAccess.getPos())) {
      FractalSkylandsTweaks.getLogger().debug(
        String.format(
          "Expanding island placement generation from %d to %d because chunk %s (block %d,%d) is outside the boundary of %d",
          this.currentGenerationSize,
          this.currentGenerationSize + this.poissonGenerationStep,
          chunkAccess.getPos(),
          chunkAccess.getPos().getMiddleBlockX(),
          chunkAccess.getPos().getMiddleBlockZ(),
          this.currentGenerationSize - largestConfiguredRadius
        )
      );

      long start = System.currentTimeMillis();
      // Check if we're outside currently generated chunks, if so, increase the generation size
      this.currentGenerationSize += this.poissonGenerationStep;
      // We're on a step-by-step algorithm, so we need to generate islands
      // in the new area before making that area bigger again
      getIslands(seed);
      long end = System.currentTimeMillis();

      FractalSkylandsTweaks.getLogger().debug("Expansion took " + (end - start) + "ms");
    }
  }

  public void addIsland(Point<IslandConfiguration> island, long seed) {
    IslandConfiguration config = island.config.data;
    cachedIslands.add(createIsland(config, island.position.toBlockPos(0), seed));
  }

  public List<Island> getIslands(long seed) {
    List<Point<IslandConfiguration>> newIslands = getNewIslandPlacements(seed);
  
    if (newIslands.size() > 0) {
      for (Point<IslandConfiguration> point : newIslands) {
        this.addIsland(point, seed);
      }
    }
  
    return cachedIslands;
  }

  public boolean hasGeneratedIslands() {
    return this.generatedIslandPositions.size() > 0;
  }
}
