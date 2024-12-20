package dev.cernavskis.fractalskylandstweaks.core.event;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import net.minecraftforge.eventbus.api.IEventBus;

public abstract class EventListener {

    public void onModInitialization(IEventBus eventBus) {
        FractalSkylandsTweaks.getLogger().info("Registering events in {}...", this.getClass().getSimpleName());
        eventBus.register(this);
    }
}
