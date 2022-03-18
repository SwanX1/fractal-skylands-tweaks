// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class IslandSurfaceSettings {
  public double noiseScale;
  public double heightMultiplier;
  public BlockStateSupplier blockStateSupplier;

  public IslandSurfaceSettings(double noiseScale, double heightMultiplier, BlockStateSupplier blockStateSupplier) {
    this.noiseScale = noiseScale;
    this.heightMultiplier = heightMultiplier;
    this.blockStateSupplier = blockStateSupplier;
  }

  @FunctionalInterface
  public static interface BlockStateSupplier {
    BlockState apply(double totalHeight, double radius, BlockPos relativePosition, long seed);
  }
}
