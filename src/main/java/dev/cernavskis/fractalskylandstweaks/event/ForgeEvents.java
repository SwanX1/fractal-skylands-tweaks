// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.event;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import dev.cernavskis.fractalskylandstweaks.command.Commands;
import dev.cernavskis.fractalskylandstweaks.world.gen.SBConfiguredFeatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ForgeEvents {
  private static final Logger LOGGER = LogManager.getLogger();

  @SubscribeEvent
  public void onCreateWorldSpawn(WorldEvent.CreateSpawnPosition event) {
    if (event.getWorld() instanceof ServerWorld) {
      ServerWorld world = (ServerWorld) event.getWorld();
      if (world.dimension() == World.OVERWORLD) {
        world.setDefaultSpawnPos(new BlockPos(0, 64, 0), 0);
        long startTime = System.nanoTime();
        SBConfiguredFeatures.START_ISLAND.place(world, world.getChunkSource().getGenerator(), new Random(world.getRandom().nextLong()), new BlockPos(0, 64, 0));
        LOGGER.debug("Generated island in {}ms", (System.nanoTime() - startTime) / 1000000);
      }
    }
  }

  @SubscribeEvent
  public void onRegisterCommands(RegisterCommandsEvent event) {
    Commands.register(event.getDispatcher());
  }

  @SubscribeEvent
  public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
    FractalSkylandsTweaks.PLAYER_STAGES.unload();
    FractalSkylandsTweaks.PLAYER_STAGES.setServer(event.getServer());
  }

  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    FractalSkylandsTweaks.PLAYER_STAGES.load();
  }

  @SubscribeEvent
  public void onWorldSave(WorldEvent.Save event) {
    FractalSkylandsTweaks.PLAYER_STAGES.save();
  }
}
