// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.World;

public class IslandUtil {
  /**
   * Generates an island at the given position.
   *
   * @param world The world to generate the island in.
   * @param random The random to use for generation.
   * @param position The position to generate the island at.
   * @param shapeSettings The settings to use for the shape of the island.
   * @param baseSettings The settings to use for the base of the island.
   * @param surfaceSettings The settings to use for the surface of the island.
   */
  public static void generateBasicIsland(IWorldWriter world, Random random, BlockPos position, IslandShapeSettings shapeSettings, IslandBaseSettings baseSettings, IslandSurfaceSettings surfaceSettings) {
    IProfiler profiler = new ProfilerWrapper(world instanceof World ? ((World) world).getProfiler() : null);
    profiler.push("fractalskylandstweaks:generate_basic_island");
    long shapeSeed = random.nextLong();
    double iterableRadius = shapeSettings.radius + shapeSettings.noiseMultiplier;

    long baseSeed = random.nextLong();
    double baseNoisePeak = 0;
    long surfaceSeed = random.nextLong();
    double surfaceNoisePeak = 0;

    for (int x = -(int) (iterableRadius / (baseSettings.noiseScale * 8)); x < iterableRadius / (baseSettings.noiseScale * 8); x++) {
      for (int z = -(int) (iterableRadius / (baseSettings.noiseScale * 8)); z < shapeSettings.radius / (baseSettings.noiseScale * 8); z++) {
        double noise = OpenSimplex2F.noise2(baseSeed, x * baseSettings.noiseScale, z * baseSettings.noiseScale);
        if (noise > baseNoisePeak) {
          baseNoisePeak = noise;
        }
      }
    }
    for (int x = -(int) (iterableRadius / (surfaceSettings.noiseScale * 8)); x < iterableRadius / (surfaceSettings.noiseScale * 8); x++) {
      for (int z = -(int) (iterableRadius / (surfaceSettings.noiseScale * 8)); z < iterableRadius / (surfaceSettings.noiseScale * 8); z++) {
        double noise = OpenSimplex2F.noise2(surfaceSeed, x * surfaceSettings.noiseScale, z * surfaceSettings.noiseScale);
        if (noise > surfaceNoisePeak) {
          surfaceNoisePeak = noise;
        }
      }
    }

    long surfaceBlockStateSeed = random.nextLong();
    long baseBlockStateSeed = random.nextLong();
    for (int x = -(int) iterableRadius; x < iterableRadius; x++) {
      for (int z = -(int) iterableRadius; z < iterableRadius; z++) {
        double distance = Math.sqrt(x * x + z * z);
        double angle = MathHelper.atan2(z, x);
        double shapeNoise = OpenSimplex2F.noise2(shapeSeed, MathHelper.cos((float) angle) * shapeSettings.noiseScale, MathHelper.sin((float) angle) * shapeSettings.noiseScale) * shapeSettings.noiseMultiplier;
        double actualRadius = shapeSettings.radius + shapeNoise;
        if (distance >= actualRadius) {
          continue;
        }

        double surfaceNoise = OpenSimplex2F.noise2(surfaceSeed, x * surfaceSettings.noiseScale, z * surfaceSettings.noiseScale);
        double height = (Math.abs(surfaceNoise - surfaceNoisePeak) * (actualRadius - distance) + (actualRadius - distance)) * surfaceSettings.heightMultiplier;
        for (int y = 0; y <= height; y++) {
          BlockPos pos = new BlockPos(x, y, z);
          BlockState blockState = surfaceSettings.blockStateSupplier.apply(height, actualRadius, pos, surfaceBlockStateSeed);
          if (blockState != null) {
            world.setBlock(position.offset(pos), blockState, 2);
          }
        }

        double baseNoise = OpenSimplex2F.noise2(baseSeed, x * baseSettings.noiseScale, z * baseSettings.noiseScale);
        double depth = (Math.abs(baseNoise - baseNoisePeak) * (actualRadius - distance) + (actualRadius - distance)) * baseSettings.depthMultiplier;
        for (int y = -(int) depth; y < 0; y++) {
          BlockPos pos = new BlockPos(x, y, z);
          BlockState blockState = baseSettings.blockStateSupplier.apply(depth, actualRadius, pos, baseBlockStateSeed);
          if (blockState != null) {
            world.setBlock(position.offset(pos), blockState, 2);
          }
        }
      }
    }
    profiler.pop();
  }
}
