// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class IslandBaseSettings {
  public double noiseScale;
  public double depthMultiplier;
  public BlockStateSupplier blockStateSupplier;

  public IslandBaseSettings(double noiseScale, double depthMultiplier, BlockStateSupplier blockStateSupplier) {
    this.noiseScale = noiseScale;
    this.depthMultiplier = depthMultiplier;
    this.blockStateSupplier = blockStateSupplier;
  }

  @FunctionalInterface
  public static interface BlockStateSupplier {
    BlockState apply(double totalDepth, double radius, BlockPos relativePosition, long seed);
  }
}
