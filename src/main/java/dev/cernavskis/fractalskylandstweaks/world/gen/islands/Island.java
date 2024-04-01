// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands;

import java.util.function.BiFunction;

import dev.cernavskis.fractalskylandstweaks.util.OpenSimplex2F;
import dev.cernavskis.fractalskylandstweaks.util.WorldgenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class Island {
  public final IslandConfiguration config;
  public final BlockPos position;
  private final long seed;

  public Island(IslandConfiguration config, BlockPos position, long seed) {
    this.config = config;
    this.position = position;
    this.seed = seed;
  }

  protected RandomSource getRandom() {
    return new XoroshiroRandomSource(this.seed);
  }

  public void generateTerrain(WorldGenLevel level, ChunkAccess chunkAccess, ChunkGenerator chunkGenerator) {
    RandomSource random = this.getRandom();
    
    long shapeSeed = random.nextLong();
    long baseSeed = random.nextLong();
    long surfaceSeed = random.nextLong();

    double iterableRadius = this.config.terrainSettings.radius + this.config.terrainSettings.shapeNoiseMultiplier;
    
    double baseNoisePeak = Island.getNoisePeak((x, z) -> this.getBaseNoise(baseSeed, x, z), (int) (iterableRadius / (this.config.terrainSettings.groundNoiseScale * 8)));
    double surfaceNoisePeak = Island.getNoisePeak((x, z) -> this.getSurfaceNoise(surfaceSeed, x, z), (int) (iterableRadius / (this.config.terrainSettings.surfaceNoiseScale * 8)));
    
    for (int x = -(int) iterableRadius; x < iterableRadius; x++) {
      for (int z = -(int) iterableRadius; z < iterableRadius; z++) {
        if (!WorldgenUtil.isWithinChunk(chunkAccess.getPos(), this.position.offset(x, 0, z))) continue;

        double distanceSqr = x * x + z * z;
        double angle = Math.atan2(z, x);
        double shapeNoise = this.getShapeNoise(shapeSeed, angle);
        double actualRadius = this.config.terrainSettings.radius + shapeNoise;
        if (distanceSqr >= Math.pow(actualRadius, 2)) continue;
        double distance = Math.sqrt(distanceSqr);
        
        BlockPos pos = new BlockPos(x, 0, z);

        double surfaceNoise = this.getSurfaceNoise(surfaceSeed, x, z);
        double baseNoise = this.getBaseNoise(baseSeed, x, z);

        double height = (Math.abs(surfaceNoise - surfaceNoisePeak) + 1) * (actualRadius - distance) * this.config.terrainSettings.surfaceNoiseMultiplier;
        double depth = (Math.abs(baseNoise - baseNoisePeak) + 1) * (actualRadius - distance) * this.config.terrainSettings.groundNoiseMultiplier;

        for (int y = -(int)depth; y <= height; y++) {
          BlockState blockState = this.config.blockStateSupplier.apply(height, depth, actualRadius, pos.relative(Axis.Y, y), baseSeed + 1);
          if (blockState != null) {
            level.setBlock(this.position.offset(pos.relative(Axis.Y, y)), blockState, 2);
          }
        }
      }
    }
  }

  public void generateBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, ChunkGenerator chunkGenerator) {
    // Override this method to generate biome-specific decorations.
  }

  public boolean shouldTryGenerate(ChunkPos chunk, BlockPos position, int leeway) {
    int maxPossibleDistance = (int) (this.config.terrainSettings.radius + this.config.terrainSettings.shapeNoiseMultiplier) + leeway;
    return WorldgenUtil.isWithinChunk(chunk, position, maxPossibleDistance);
  }

  protected double getAngledNoise(long seed, double angle, double scale) {
    return OpenSimplex2F.noise2(seed, Math.cos((float) angle) * scale, Math.sin((float) angle) * scale);
  }

  protected double getShapeNoise(long seed, double angle) {
    return this.getAngledNoise(seed, angle, this.config.terrainSettings.shapeNoiseScale) * this.config.terrainSettings.shapeNoiseMultiplier;
  }

  protected double getSurfaceNoise(long seed, int x, int z) {
    return OpenSimplex2F.noise2(seed, x * this.config.terrainSettings.surfaceNoiseScale, z * this.config.terrainSettings.surfaceNoiseScale);
  }

  protected double getBaseNoise(long seed, int x, int z) {
    return OpenSimplex2F.noise2(seed, x * this.config.terrainSettings.groundNoiseScale, z * this.config.terrainSettings.groundNoiseScale);
  }

  protected static double getNoisePeak(BiFunction<Integer, Integer, Double> noiseFunction, int bounds) {
    double peak = 0;
    for (int x = -bounds; x < bounds; x++) {
      for (int z = -bounds; z < bounds; z++) {
        double noise = noiseFunction.apply(x, z);
        if (noise > peak) {
          peak = noise;
        }
      }
    }
    return peak;
  }
}
