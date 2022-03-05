// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world;

import com.mojang.serialization.Lifecycle;

import dev.cernavskis.fractalskylandstweaks.world.gen.IslandChunkGenerator;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraftforge.common.world.ForgeWorldType;

public class SkyblockWorldType extends ForgeWorldType {
  public SkyblockWorldType() {
    super(null);
  }

  @Override
  public ChunkGenerator createChunkGenerator(Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry, long seed, String generatorSettings) {
    return new NoiseChunkGenerator(
      new OverworldBiomeProvider(seed, false, false, biomeRegistry),
      seed,
      () -> dimensionSettingsRegistry.getOrThrow(DimensionSettings.OVERWORLD)
    );
  }

  public static SimpleRegistry<Dimension> getDefaultSimpleRegistry(Registry<DimensionType> lookUpRegistryDimensionType, Registry<Biome> registry, Registry<DimensionSettings> dimensionSettings, long seed) {
    SimpleRegistry<Dimension> simpleRegistry = new SimpleRegistry<>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.stable());
    simpleRegistry.register(
      Dimension.OVERWORLD,
      new Dimension(
        () -> lookUpRegistryDimensionType.getOrThrow(DimensionType.OVERWORLD_LOCATION),
        new IslandChunkGenerator(seed, () -> registry.getOrThrow(Biomes.PLAINS))
      ),
      Lifecycle.stable()
    );
    simpleRegistry.register(
      Dimension.NETHER,
      new Dimension(
        () -> lookUpRegistryDimensionType.getOrThrow(DimensionType.NETHER_LOCATION),
        new IslandChunkGenerator(seed, () -> registry.getOrThrow(Biomes.NETHER_WASTES))
      ),
      Lifecycle.stable()
    );
    simpleRegistry.register(
      Dimension.END,
      new Dimension(
        () -> lookUpRegistryDimensionType.getOrThrow(DimensionType.END_LOCATION),
        new IslandChunkGenerator(seed, () -> registry.getOrThrow(Biomes.THE_END))
      ),
      Lifecycle.stable()
    );

    return simpleRegistry;
  }

  @Override
  public DimensionGeneratorSettings createSettings(DynamicRegistries dynamicRegistries, long seed, boolean generateStructures, boolean generateLoot, String generatorSettings) {
    return new DimensionGeneratorSettings(
      seed,
      generateStructures,
      generateLoot,
      getDefaultSimpleRegistry(
        dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
        dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY),
        dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
        seed
      )
    );
  }
}