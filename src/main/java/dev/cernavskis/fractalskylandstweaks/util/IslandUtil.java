// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldWriter;

public class IslandUtil {
  /**
   * Generates an island at the given position.
   *
   * @param world The world to generate the island in.
   * @param random The random to use for generation.
   * @param position The position to generate the island at.
   * @param radius The radius of the island.
   * @param baseSettings The settings to use for the base of the island.
   * @param surfaceSettings The settings to use for the surface of the island.
   */
  public static void generateBasicIsland(IWorldWriter world, Random random, BlockPos position, double radius,
      IslandBaseSettings baseSettings, IslandSurfaceSettings surfaceSettings) {
    long baseSeed = random.nextLong();
    double baseNoisePeak = 0;
    for (int x = -(int) (radius / (baseSettings.noiseScale * 8)); x < radius / (baseSettings.noiseScale * 8); x++) {
      for (int z = -(int) (radius / (baseSettings.noiseScale * 8)); z < radius / (baseSettings.noiseScale * 8); z++) {
        double noise = OpenSimplex2F.noise2(baseSeed, x * baseSettings.noiseScale, z * baseSettings.noiseScale);
        if (noise > baseNoisePeak) {
          baseNoisePeak = noise;
        }
      }
    }

    long baseBlockStateSeed = random.nextLong();
    for (int x = -(int) radius; x < radius; x++) {
      for (int z = -(int) radius; z < radius; z++) {
        double distance = Math.sqrt(x * x + z * z);
        if (distance >= radius) {
          continue;
        }

        double noise = OpenSimplex2F.noise2(baseSeed, x * baseSettings.noiseScale, z * baseSettings.noiseScale);
        double depth = (Math.abs(noise - baseNoisePeak) * (radius - distance) + (radius - distance)) * baseSettings.depthMultiplier;

        for (int y = -(int) depth; y < 0; y++) {
          BlockPos pos = new BlockPos(x, y, z);
          BlockState blockState = baseSettings.blockStateSupplier.apply(depth, radius, pos, baseBlockStateSeed);
          if (blockState != null) {
            world.setBlock(position.offset(pos), blockState, 2);
          }
        }
      }
    }

    long surfaceSeed = random.nextLong();
    double surfaceNoisePeak = 0;
    for (int x = -(int) (radius / (surfaceSettings.noiseScale * 8)); x < radius / (surfaceSettings.noiseScale * 8); x++) {
      for (int z = -(int) (radius / (surfaceSettings.noiseScale * 8)); z < radius / (surfaceSettings.noiseScale * 8); z++) {
        double noise = OpenSimplex2F.noise2(surfaceSeed, x * surfaceSettings.noiseScale, z * surfaceSettings.noiseScale);
        if (noise > surfaceNoisePeak) {
          surfaceNoisePeak = noise;
        }
      }
    }

    long surfaceBlockStateSeed = random.nextLong();
    for (int x = -(int) radius; x < radius; x++) {
      for (int z = -(int) radius; z < radius; z++) {
        double distance = Math.sqrt(x * x + z * z);
        if (distance >= radius) {
          continue;
        }

        double noise = OpenSimplex2F.noise2(surfaceSeed, x * surfaceSettings.noiseScale, z * surfaceSettings.noiseScale);
        double height = (Math.abs(noise - surfaceNoisePeak) * (radius - distance) + (radius - distance)) * surfaceSettings.depthMultiplier;

        for (int y = 0; y <= height; y++) {
          BlockPos pos = new BlockPos(x, y, z);
          BlockState blockState = surfaceSettings.blockStateSupplier.apply(height, radius, pos, surfaceBlockStateSeed);
          if (blockState != null) {
            world.setBlock(position.offset(pos), blockState, 2);
          }
        }
      }
    }
  }
}
