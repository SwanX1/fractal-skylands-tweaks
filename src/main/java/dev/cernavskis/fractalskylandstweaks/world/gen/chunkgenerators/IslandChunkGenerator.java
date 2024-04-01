// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.chunkgenerators;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.cernavskis.fractalskylandstweaks.core.FSTIslandConfigurations;
import dev.cernavskis.fractalskylandstweaks.util.WorldgenUtil;
import dev.cernavskis.fractalskylandstweaks.util.math.Vector2d;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.Point;
import dev.cernavskis.fractalskylandstweaks.util.math.poisson.PointConfiguration;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.Island;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.provider.IslandProvider;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;

public class IslandChunkGenerator extends SimpleChunkGenerator {
	public static final Codec<IslandChunkGenerator> CODEC = RecordCodecBuilder.create(
    builder -> builder.group(
      BiomeSource.CODEC.fieldOf("biome_source").forGetter((instance) -> instance.biomeSource),
      Codec.INT.fieldOf("max_island_distance").forGetter((instance) -> instance.islandProvider.maxIslandDistance),
      Codec.INT.fieldOf("poisson_generation_step").forGetter((instance) -> instance.islandProvider.poissonGenerationStep)
    ).apply(builder, builder.stable(IslandChunkGenerator::new))
  );

  private final IslandProvider islandProvider;

  public IslandChunkGenerator(BiomeSource biomeSource, int maxIslandDistance, int poissonGenerationStep) {
    super(biomeSource);
    this.islandProvider = new IslandProvider(maxIslandDistance, poissonGenerationStep);
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  // Below this are implementations of ChunkGenerator methods

  @Override
  public void buildSurface(WorldGenRegion world, StructureManager structureManager, RandomState random, ChunkAccess chunkAccess) {
    if (this.islandProvider.generatedIslandPositions.size() == 0) {
      // We add this directly to firstly not generate any islands within the starting island
      // and secondly to ensure that new islands aren't generated in some čuhņa
      Point<IslandConfiguration> startIslandPoint = new Point<>(
        Vector2d.fromBlockPos(WorldgenUtil.getWorldSpawn(world.getLevelData())),
        new PointConfiguration<>(FSTIslandConfigurations.START_ISLAND.minDistance, 1, FSTIslandConfigurations.START_ISLAND)
      );

      this.islandProvider.addIsland(startIslandPoint, world.getSeed());
      this.islandProvider.addIslandPlacement(startIslandPoint);
    }

    this.islandProvider.ensureIslandPlacements(world.getSeed(), chunkAccess);

    List<Island> islands = this.islandProvider.getIslands(world.getSeed());

    for (Island island : islands) {
      if (WorldgenUtil.isWithinChunk(chunkAccess.getPos(), island.position, IslandConfiguration.getMaxPossibleRadius(island.config))) {
        island.generateTerrain(world, chunkAccess, this);
      }
    }
  }

  @Override
  public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunkAccess, StructureManager structureManager) {    
    this.islandProvider.ensureIslandPlacements(world.getSeed(), chunkAccess);

    List<Island> islands = this.islandProvider.getIslands(world.getSeed());

    for (Island island : islands) {
      if (WorldgenUtil.isWithinChunk(chunkAccess.getPos(), island.position, IslandConfiguration.getMaxPossibleRadius(island.config))) {
        island.generateBiomeDecoration(world, chunkAccess, this);
      }
    }
  }
}
