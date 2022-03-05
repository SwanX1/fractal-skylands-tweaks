// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.world.gen;

import dev.cernavskis.fractalskylandstweaks.world.gen.features.StartIslandFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SBFeatures {
  public static final Feature<NoFeatureConfig> START_ISLAND = new StartIslandFeature(NoFeatureConfig.CODEC);
}
