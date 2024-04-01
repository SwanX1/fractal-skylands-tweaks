// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;

import dev.cernavskis.fractalskylandstweaks.core.FSTChunkGenerators;
import dev.cernavskis.fractalskylandstweaks.core.FSTLootConditions;
import dev.cernavskis.fractalskylandstweaks.core.event.ForgeEvents;
import dev.cernavskis.fractalskylandstweaks.tileentity.MillstoneBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FractalSkylandsTweaks.MOD_ID)
public class FractalSkylandsTweaks {
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String MOD_ID = "fractalskylandstweaks";

  public FractalSkylandsTweaks() {
    System.out.println("Initializing Fractal Skylands Tweaks");
    System.out.println("Registering events...");
    IEventBus eventBus = MinecraftForge.EVENT_BUS;
    eventBus.register(new ForgeEvents());

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    FSTChunkGenerators.registerToBus(modEventBus);
    FSTLootConditions.registerToBus(modEventBus);

    eventBus.addGenericListener(
      MillstoneBlockEntity.class,
      (BlockEntityBehaviourEvent<MillstoneBlockEntity> event) ->
        event.attach(new MillstoneBehaviour(event.getBlockEntity()))
    );
  }
}
