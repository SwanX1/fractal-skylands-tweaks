// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.CheckForNull;
import net.minecraft.nbt.CompoundTag;

public final class NBTUtil {

    private NBTUtil() {} // Uninstantiable

    @CheckForNull
    public static CompoundTag getOrCreateCompound(CompoundTag nbt, String path) {
        if (!nbt.contains(path, 10)) {
            nbt.put(path, new CompoundTag());
        }
        return nbt.getCompound(path);
    }

    public static List<String> getStringArray(CompoundTag tag) {
        int size = tag.getInt("size");
        List<String> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            list.add(tag.getString(Integer.toString(i)));
        }

        return list;
    }

    public static void putStringArray(CompoundTag tag, List<String> list) {
        tag.putInt("size", list.size());
        for (int i = 0; i < list.size(); i++) {
            tag.putString(Integer.toString(i), list.get(i));
        }
    }
}
