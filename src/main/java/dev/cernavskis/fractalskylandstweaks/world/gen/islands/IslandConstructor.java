// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface IslandConstructor {
    Island create(IslandConfiguration config, BlockPos position, long seed);
}
