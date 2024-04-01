// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math;

import net.minecraft.core.BlockPos;

public class Vector2d {
  public final double x;
  public final double y;

  public Vector2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double distanceSqr(Vector2d other) {
    double dx = this.x - other.x;
    double dy = this.y - other.y;
    return dx * dx + dy * dy;
  }

  public double distance(Vector2d other) {
    return Math.sqrt(this.distanceSqr(other));
  }
  
  public boolean isWithinDistance(Vector2d other, double distance) {
    double distanceSqr = this.distanceSqr(other);
    double distanceThreshold = distance * distance;
    return distanceSqr <= distanceThreshold;
  }

  public BlockPos toBlockPos(int y) {
    return new BlockPos((int) this.x, y, (int) this.y);
  }

  public static Vector2d fromBlockPos(BlockPos pos) {
    return new Vector2d(pos.getX(), pos.getZ());
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}