// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world.gen.biome.provider;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

public class StaticBiomeProvider extends BiomeProvider {
  public static final Codec<StaticBiomeProvider> CODEC = RecordCodecBuilder.create(
    builder -> builder.group(
      Biome.CODEC.fieldOf("biome").forGetter((instance) -> () -> instance.getBiome())
    ).apply(builder, StaticBiomeProvider::new)
  );

  public StaticBiomeProvider(Supplier<Biome> biome) {
    this(biome.get());
  }

  public StaticBiomeProvider(Biome biome) {
    super(new ArrayList<Biome>(1) {{
      add(biome);
    }});
  }


  public Biome getBiome() {
    return possibleBiomes().get(0);
  }

  @Override
  public Biome getNoiseBiome(int x, int y, int z) {
    return getBiome();
  }

  @Override
  protected Codec<? extends BiomeProvider> codec() {
    return CODEC;
  }

  @Override
  public BiomeProvider withSeed(long seed) {
    return this;
  }
}
