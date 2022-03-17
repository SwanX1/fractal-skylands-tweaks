// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world.gen.features;

import java.util.Random;

import com.mojang.serialization.Codec;

import dev.cernavskis.fractalskylandstweaks.util.IslandBaseSettings;
import dev.cernavskis.fractalskylandstweaks.util.IslandSurfaceSettings;
import dev.cernavskis.fractalskylandstweaks.util.IslandUtil;
import dev.cernavskis.fractalskylandstweaks.util.OpenSimplex2F;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class StartIslandFeature extends Feature<NoFeatureConfig> {
  public StartIslandFeature(Codec<NoFeatureConfig> codec) {
    super(codec);
  }

  public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
    double radius = 7.5 + random.nextDouble();
    IslandUtil.generateBasicIsland(
      world,
      random,
      position,
      radius,
      new IslandBaseSettings(0.0625, 0.9, StartIslandFeature::getBaseBlock),
      new IslandSurfaceSettings(1, 0, StartIslandFeature::getSurfaceBlock)
    );

    // Add generation features
    BlockPos treePos = new BlockPos(
      (random.nextInt((int) (radius / 2))) * (random.nextBoolean() ? -1 : 1),
      1,
      (random.nextInt((int) (radius / 2))) * (random.nextBoolean() ? -1 : 1)
    );
    Features.FANCY_OAK.place(world, generator, random, position.offset(treePos));

    return true;
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

  private static BlockState getSurfaceBlock(double altitude, double radius, BlockPos relativePosition, long seed) {
    return altitude - relativePosition.getY() < 0.5 ? Blocks.GRASS_BLOCK.defaultBlockState() : Blocks.DIRT.defaultBlockState();
  }
}
