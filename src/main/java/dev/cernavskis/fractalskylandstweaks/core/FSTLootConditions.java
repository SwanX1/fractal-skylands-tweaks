// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import dev.cernavskis.fractalskylandstweaks.data.conditions.TetraToolLevelItemCondition;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class FSTLootConditions {
private static final DeferredRegister<LootItemConditionType> REGISTRY = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, FractalSkylandsTweaks.MOD_ID);

public static final LootItemConditionType TETRA_TOOL_LEVEL = register("tetra_tool_level", new LootItemConditionType(new TetraToolLevelItemCondition.TSerializer()));

  private static LootItemConditionType register(String name, LootItemConditionType conditionType) {
    REGISTRY.register(name, () -> conditionType);
    return conditionType;
  }

  public static void registerToBus(IEventBus bus) {
    REGISTRY.register(bus);
  }
}
