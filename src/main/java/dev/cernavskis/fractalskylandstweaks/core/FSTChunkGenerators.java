// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core;

import com.mojang.serialization.Codec;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import dev.cernavskis.fractalskylandstweaks.world.gen.chunkgenerators.IslandChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FSTChunkGenerators {
  private static final DeferredRegister<Codec<? extends ChunkGenerator>> REGISTRY = DeferredRegister.create(Registries.CHUNK_GENERATOR, FractalSkylandsTweaks.MOD_ID);

  public static final RegistryObject<Codec<? extends ChunkGenerator>> ISLAND = register("island", IslandChunkGenerator.CODEC);

  private static RegistryObject<Codec<? extends ChunkGenerator>> register(String name, Codec<? extends ChunkGenerator> codec) {
    return REGISTRY.register(name, () -> codec);
  }

  public static void registerToBus(IEventBus bus) {
    REGISTRY.register(bus);
  }
}
