// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world.gen;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.cernavskis.fractalskylandstweaks.world.gen.biome.provider.StaticBiomeProvider;
import net.minecraft.block.BlockState;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class IslandChunkGenerator extends ChunkGenerator {
  public static Codec<IslandChunkGenerator> CODEC = RecordCodecBuilder.create(
    builder -> builder.group(
      Codec.LONG.fieldOf("seed").stable().forGetter((instance) -> instance.seed),
      StaticBiomeProvider.CODEC.fieldOf("biome_provider").forGetter((instance) -> (StaticBiomeProvider) instance.biomeSource)
    ).apply(builder, IslandChunkGenerator::new)
  );

  private final long seed;

  public IslandChunkGenerator(long seed, Supplier<Biome> biome) {
    this(seed, new StaticBiomeProvider(biome));
  }

  public IslandChunkGenerator(long seed, StaticBiomeProvider biomeProvider) {
    super(biomeProvider, new DimensionStructuresSettings(false));
    this.seed = seed;
  }

  @Override
  protected Codec<? extends ChunkGenerator> codec() {
    return CODEC;
  }

  @Override
  public ChunkGenerator withSeed(long seed) {
    return new IslandChunkGenerator(seed, (StaticBiomeProvider) this.biomeSource);
  }

  @Override
  public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
    // We don't actually need to do anything here
  }

  @Override
  public void fillFromNoise(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {
    // Yeah no thanks
  }

  @Override
  public int getBaseHeight(int p_222529_1_, int p_222529_2_, Type p_222529_3_) {
    return 0;
  }

  @Override
  public IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
    return new Blockreader(new BlockState[0]);
  }

}
