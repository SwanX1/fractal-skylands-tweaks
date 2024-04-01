// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands.custom;

import java.util.LinkedList;
import java.util.List;

import dev.cernavskis.fractalskylandstweaks.util.WorldgenUtil;
import dev.cernavskis.fractalskylandstweaks.util.math.OpenSimplex2F;
import dev.cernavskis.fractalskylandstweaks.util.math.Vector2i;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.Island;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class StartIsland extends Island {
  public StartIsland(IslandConfiguration config, BlockPos position, long seed) {
    super(config, position, seed);
  }

  @Override
  public void generateBiomeDecoration(WorldGenLevel world, ChunkAccess chunkAccess, ChunkGenerator chunkGenerator) {
    RandomSource random = this.getRandom();

    double treePossibleRadius = this.config.terrainSettings.radius * (0.5 + (random.nextDouble() * this.config.terrainSettings.surfaceNoiseMultiplier));
    BlockPos treePos = new BlockPos(  
      (random.nextInt((int) (treePossibleRadius / 2) + 1) + 3) * (random.nextBoolean() ? -1 : 1),
      1,
      (random.nextInt((int) (treePossibleRadius / 2) + 1) + 3) * (random.nextBoolean() ? -1 : 1)
    ).offset(this.position);

    if (WorldgenUtil.isWithinChunk(chunkAccess.getPos(), treePos)) {
      ConfiguredFeature<?, ?> treeFeature = world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(TreeFeatures.FANCY_OAK);
      treeFeature.place(world, chunkGenerator, random, treePos);
    }

    // this.generatePond(world, chunkAccess, this.position);
  }

  @SuppressWarnings("unused") // TODO: doesn't look good
  private void generatePond(WorldGenLevel level, ChunkAccess chunkAccess, BlockPos position) {
    RandomSource random = this.getRandom();
    long pondSeed = random.nextLong() ^ "pond".hashCode();

    int radius = 2 + random.nextInt(1);

    List<Vector2i> columnPositions = new LinkedList<>();

    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {
        double distanceSqr = x * x + z * z;
        double angle = Math.atan2(z, x);
        double shapeNoise = this.getAngledNoise(pondSeed, angle, 0.025);
        double actualRadius = this.config.terrainSettings.radius + shapeNoise;
        if (distanceSqr >= Math.pow(actualRadius, 2)) continue;

        int depth = (int) ((Math.pow(radius, 2) - distanceSqr) / radius);
        BlockPos pos = new BlockPos(x, 0, z);

        for (int y = 0; y > -depth; y--) {
          level.setBlock(pos.relative(Axis.Y, y).offset(position), Blocks.WATER.defaultBlockState(), 2);
        }

        columnPositions.add(new Vector2i(x, z));
      }
    }

    // Generate lily pads
    int lilyPadCount = random.nextInt(2);
    for (int i = 0; i < lilyPadCount; i++) {
      Vector2i pos = columnPositions.get(random.nextInt(columnPositions.size()));
      columnPositions.remove(pos);
      BlockPos lilyPadPos = pos.toBlockPos(1).offset(position);
      level.setBlock(lilyPadPos, Blocks.LILY_PAD.defaultBlockState(), 2);
    }
  }

  public static BlockState getBlockState(double height, double depth, double radius, BlockPos relativePosition, long seed) {
    return relativePosition.getY() < 0 ? getBaseBlock(depth, radius, relativePosition, seed) : getSurfaceBlock(height, radius, relativePosition, seed);
  }

  private static BlockState getBaseBlock(double depth, double radius, BlockPos relativePosition, long seed) {
    int x = relativePosition.getX();
    int y = relativePosition.getY();
    int z = relativePosition.getZ();
    double noise2 = OpenSimplex2F.noise2(seed, x * 0.0625, z * 0.0625);
    if (relativePosition.getY() >= -2 + (noise2 < 0.5 ? 1 : 0)) {
      return Blocks.DIRT.defaultBlockState();
    }
    double noise3 = OpenSimplex2F.noise3_ImproveXZ(seed, x * 0.15, y * 0.15, z * 0.15);
    boolean shouldBeAndesite = noise3 < 0;
    if (!shouldBeAndesite) {
      double distance = Math.sqrt(x * x + y * y + z * z);
      if (distance < radius * 0.75) {
        shouldBeAndesite = true;
      }
    }
    return shouldBeAndesite ? Blocks.ANDESITE.defaultBlockState() : Blocks.STONE.defaultBlockState();
  }

  private static BlockState getSurfaceBlock(double height, double radius, BlockPos relativePosition, long seed) {
    return height - relativePosition.getY() < 0.5 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.DIRT.defaultBlockState();
  }
}
