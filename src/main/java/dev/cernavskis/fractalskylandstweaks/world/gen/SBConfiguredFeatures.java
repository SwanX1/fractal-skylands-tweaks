// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world.gen;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SBConfiguredFeatures {
  public static final ConfiguredFeature<NoFeatureConfig, ?> START_ISLAND = new ConfiguredFeature<>(SBFeatures.START_ISLAND, NoFeatureConfig.NONE);
}
