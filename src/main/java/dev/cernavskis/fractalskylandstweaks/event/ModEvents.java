// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks.MOD_ID;
import dev.cernavskis.fractalskylandstweaks.world.SkyblockWorldType;
import dev.cernavskis.fractalskylandstweaks.world.gen.IslandChunkGenerator;
import dev.cernavskis.fractalskylandstweaks.world.gen.biome.provider.StaticBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModEvents {
  private static final Logger LOGGER = LogManager.getLogger();

  @SubscribeEvent
  public void onRegisterWorldTypes(RegistryEvent.Register<ForgeWorldType> event) {
    LOGGER.info("Registering world types...");
    event.getRegistry().register(new SkyblockWorldType().setRegistryName(new ResourceLocation(MOD_ID, "skyblock_world_type")));
  }

  @SubscribeEvent
  public void onCommonSetup(FMLCommonSetupEvent event) {
    LOGGER.info("Registering world types...");
    event.enqueueWork(() -> {
      Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MOD_ID, "static_biome_provider"), StaticBiomeProvider.CODEC);
      Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MOD_ID, "island_chunk_generator"), IslandChunkGenerator.CODEC);
    });
  }
}
