// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks;

import com.simibubi.create.api.event.TileEntityBehaviourEvent;
import com.simibubi.create.content.contraptions.components.millstone.MillstoneTileEntity;
import dev.cernavskis.fractalskylandstweaks.tileentity.MillstoneBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.cernavskis.fractalskylandstweaks.event.ForgeEvents;
import dev.cernavskis.fractalskylandstweaks.event.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FractalSkylandsTweaks.MOD_ID)
public class FractalSkylandsTweaks {
  private static final Logger LOGGER = LogManager.getLogger();
  public static final String MOD_ID = "fractalskylandstweaks";

  public FractalSkylandsTweaks() {
    LOGGER.info("Initializing Fractal Skylands Tweaks");
    LOGGER.info("Registering events...");
    IEventBus eventBus = MinecraftForge.EVENT_BUS;
    eventBus.register(new ForgeEvents());
    FMLJavaModLoadingContext.get().getModEventBus().register(new ModEvents());

    eventBus.addGenericListener(MillstoneTileEntity.class, (TileEntityBehaviourEvent<MillstoneTileEntity> event) -> event.attach(new MillstoneBehaviour(event.getTileEntity())));
  }
}
