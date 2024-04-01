// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math;

import net.minecraft.core.BlockPos;

public class Vector2i {
  public final int x;
  public final int y;

  public Vector2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public BlockPos toBlockPos(int y) {
    return new BlockPos(this.x, y, this.y);
  }

  public static Vector2i fromBlockPos(BlockPos pos) {
    return new Vector2i(pos.getX(), pos.getZ());
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}
