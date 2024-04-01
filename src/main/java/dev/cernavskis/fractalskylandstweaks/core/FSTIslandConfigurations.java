// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core;

import dev.cernavskis.fractalskylandstweaks.world.gen.islands.Island;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandTerrainSettings;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.IslandConfiguration.BlockStateSupplier;
import dev.cernavskis.fractalskylandstweaks.world.gen.islands.custom.StartIsland;
import net.minecraft.world.level.block.Blocks;

public class FSTIslandConfigurations {

  public static IslandConfiguration START_ISLAND = new IslandConfiguration(
    new IslandTerrainSettings(
      14, 0.0625, 0.5,
      1, 0,
      0.0625, 0.9
    ),
    StartIsland::new
  )
    .withMinDistance(256)
    .withAltitude(120)
    .withBlockStateSupplier(StartIsland::getBlockState);

    
  public static IslandConfiguration SOME_BIGGER_ISLAND = new IslandConfiguration(
    new IslandTerrainSettings(
      8, 0.0625, 0.5,
      1, 0,
      0.0625, 0.7
    ),
    StartIsland::new
  )
    .withMinDistance(128)
    .withAltitude(80)
    .withAltitudeVariance(10)
    .withBlockStateSupplier(StartIsland::getBlockState)
    .withWeight(5);

  public static IslandConfiguration EXAMPLE_ISLAND = new IslandConfiguration(
    new IslandTerrainSettings(
      5, 0.0625, 0.5,
      1, 0,
      .03125, 0.8
    ),
    Island::new
  )
    .withMinDistance(64)
    .withAltitude(70)
    .withAltitudeVariance(20)
    .withBlockStateSupplier(((BlockStateSupplier) (height, depth, radius, relativePosition, seed) -> Blocks.STONE.defaultBlockState()))
    .withWeight(1);
}
