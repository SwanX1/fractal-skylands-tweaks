// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.util.math.BlockPos;

public class Util {
  public static ArrayList<BlockPos> createFilledCircle(double radius) {
    return createFilledCircle(radius, new ArrayList<>());
  }

  public static <T extends Collection<BlockPos>> T createFilledCircle(double radius, T initialList) {
    for (int x = -(int) radius; x <= radius; x++) {
      for (int z = -(int) radius; z <= radius; z++) {
        if (x * x + z * z <= radius * radius) {
          initialList.add(new BlockPos(x, 0, z));
        }
      }
    }
    return initialList;
  }
}
