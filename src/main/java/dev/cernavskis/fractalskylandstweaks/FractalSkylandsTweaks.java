// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks;

import dev.cernavskis.fractalskylandstweaks.core.event.EventListener;
import dev.cernavskis.fractalskylandstweaks.core.event.ForgeEvents;
import dev.cernavskis.fractalskylandstweaks.core.event.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FractalSkylandsTweaks.MOD_ID)
public class FractalSkylandsTweaks {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "fractalskylandstweaks";

    public FractalSkylandsTweaks() {
        FractalSkylandsTweaks.getLogger().info("Initializing Fractal Skylands Tweaks");

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        EventListener forgeEvents = new ForgeEvents();
        forgeEvents.onModInitialization(forgeEventBus);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventListener modEvents = new ModEvents();
        modEvents.onModInitialization(modEventBus);
    }

    public static Logger getLogger() {
        // Check if the calling class is in the same package as this class
        StackTraceElement callingClass = Thread.currentThread().getStackTrace()[2];
        String ourPackage = FractalSkylandsTweaks.class.getPackageName();

        if (!callingClass.getClassName().startsWith(ourPackage)) {
            throw new SecurityException("A class outside of the " + ourPackage + " package tried to access the logger!");
        }
        return FractalSkylandsTweaks.LOGGER;
    }
}
