package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.CheckForNull;

public final class NBTUtil {
  private NBTUtil() {} // Uninstantiable

  @CheckForNull
  public static CompoundNBT getOrCreateCompound(CompoundNBT nbt, String path) {
    if (!nbt.contains(path, 10)) {
      nbt.put(path, new CompoundNBT());
    }
    return nbt.getCompound(path);
  }
}
