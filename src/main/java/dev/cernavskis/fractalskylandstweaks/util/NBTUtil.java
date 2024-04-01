// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import javax.annotation.CheckForNull;

import net.minecraft.nbt.CompoundTag;

public final class NBTUtil {
  private NBTUtil() {
  } // Uninstantiable

  @CheckForNull
  public static CompoundTag getOrCreateCompound(CompoundTag nbt, String path) {
    if (!nbt.contains(path, 10)) {
      nbt.put(path, new CompoundTag());
    }
    return nbt.getCompound(path);
  }
}
