// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;

public final class RegistryUtil {

    private static MinecraftServer SERVER;

    private RegistryUtil() {} // Uninstantiable

    // SERVER ONLY!!!
    public static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        return SERVER.registryAccess().registry(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key.location()));
    }

    public static void setServer(MinecraftServer server) {
        RegistryUtil.SERVER = server;
    }
}
