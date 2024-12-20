// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.chunkgenerators;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Simple chunk generator that implements safe defaults.
 * Generates nothing.
 */
public abstract class SimpleChunkGenerator extends ChunkGenerator {

    public SimpleChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public int getSeaLevel() {
        return -63;
    }

    @Override
    public int getMinY() {
        return -63;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapTypes, LevelHeightAccessor heightAccessor, RandomState random) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState random) {
        // If we provide an empty array, the game will not be able to place any blocks and therefore crash
        BlockState[] column = new BlockState[heightAccessor.getHeight()];
        for (int i = 0; i < column.length; i++) column[i] = Blocks.AIR.defaultBlockState();
        return new NoiseColumn(heightAccessor.getMinBuildHeight(), column);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
        Executor executor,
        Blender blender,
        RandomState random,
        StructureManager structureManager,
        ChunkAccess chunkAccess
    ) {
        return CompletableFuture.completedFuture(chunkAccess); // Do nothing
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState random, BlockPos pos) {}

    @Override
    public void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structureManager) {}

    @Override
    public void applyCarvers(
        WorldGenRegion worldgenRegion,
        long seed,
        RandomState random,
        BiomeManager biomeManager,
        StructureManager structureManager,
        ChunkAccess chunkAccess,
        GenerationStep.Carving carving
    ) {}

    @Override
    public void buildSurface(WorldGenRegion world, StructureManager structureManager, RandomState random, ChunkAccess chunkAccess) {}

    @Override
    public void createReferences(WorldGenLevel p_223077_, StructureManager p_223078_, ChunkAccess p_223079_) {}

    @Override
    public void createStructures(
        RegistryAccess p_255835_,
        ChunkGeneratorStructureState p_256505_,
        StructureManager p_255934_,
        ChunkAccess p_255767_,
        StructureTemplateManager p_255832_
    ) {}

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldgenRegion) {}
}
