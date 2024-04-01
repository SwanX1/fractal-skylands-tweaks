// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core.event;

import dev.cernavskis.fractalskylandstweaks.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEvents {
  @SubscribeEvent
  public void onServerStart(ServerAboutToStartEvent event) {
    RegistryUtil.setServer(event.getServer());
  }

  @SubscribeEvent
  public void onCreateWorldSpawn(LevelEvent.CreateSpawnPosition event) {
    if (event.getLevel() instanceof ServerLevel level) {
      if (level.dimension() == Level.OVERWORLD) {
        BlockPos worldSpawn = level.getSharedSpawnPos();
        level.setDefaultSpawnPos(worldSpawn.atY(80), 0);
      }
    }
  }
}
