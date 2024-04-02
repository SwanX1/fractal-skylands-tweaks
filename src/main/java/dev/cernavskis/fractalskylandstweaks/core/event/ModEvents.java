// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core.event;

import dev.cernavskis.fractalskylandstweaks.core.FSTChunkGenerators;
import dev.cernavskis.fractalskylandstweaks.core.FSTLootConditions;
import dev.cernavskis.fractalskylandstweaks.integrations.ftbquests.FSTTaskTypes;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModEvents extends EventListener {
  @Override
  public void onModInitialization(IEventBus modEventBus) {
    FSTChunkGenerators.registerToBus(modEventBus);
    FSTLootConditions.registerToBus(modEventBus);

    // Classload FTBQuest custom tasks
    FSTTaskTypes.classload();
  }
}
