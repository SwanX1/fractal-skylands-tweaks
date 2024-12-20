// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;

public class IslandConfiguration {

    public final IslandTerrainSettings terrainSettings;
    private final IslandConstructor constructor;

    public int minDistance;
    public int weight = 0;
    public int altitude = 80;
    public int altitudeVariance = 0;
    public BlockStateSupplier blockStateSupplier = (height, depth, radius, relativePosition, seed) -> null;
    public ResourceKey<Biome> biome = Biomes.THE_VOID;
    public int biomeDistance = 0;

    public IslandConfiguration(IslandTerrainSettings terrainSettings, IslandConstructor constructor) {
        this.terrainSettings = terrainSettings;
        this.constructor = constructor;
    }

    public IslandConfiguration withMinDistance(int minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public IslandConfiguration withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public IslandConfiguration withAltitude(int altitude) {
        this.altitude = altitude;
        return this;
    }

    public IslandConfiguration withAltitudeVariance(int altitudeVariance) {
        this.altitudeVariance = altitudeVariance;
        return this;
    }

    public IslandConfiguration withBlockStateSupplier(BlockStateSupplier blockStateSupplier) {
        this.blockStateSupplier = blockStateSupplier;
        return this;
    }

    public IslandConfiguration withBiome(ResourceKey<Biome> biome) {
        this.biome = biome;
        return this;
    }

    public IslandConfiguration withBiomeDistance(int distance) {
        this.biomeDistance = distance;
        return this;
    }

    public Island create(BlockPos position, long seed) {
        return this.constructor.create(this, position, seed);
    }

    public static int getMaxPossibleRadius(IslandConfiguration configuration) {
        return (int) Math.ceil(configuration.terrainSettings.radius + configuration.terrainSettings.surfaceNoiseMultiplier);
    }

    @FunctionalInterface
    public static interface BlockStateSupplier {
        BlockState apply(double height, double depth, double radius, BlockPos relativePosition, long seed);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + terrainSettings.hashCode();
        result = prime * result + minDistance;
        result = prime * result + weight;
        result = prime * result + altitude;
        return result;
    }
}
