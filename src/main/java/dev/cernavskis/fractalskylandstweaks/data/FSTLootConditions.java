package dev.cernavskis.fractalskylandstweaks.data;

import com.mojang.datafixers.util.Pair;
import dev.cernavskis.fractalskylandstweaks.data.conditions.TetraToolLevelCondition;
import dev.cernavskis.fractalskylandstweaks.event.ModEvents;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks.MOD_ID;

public class FSTLootConditions {
    public static final LootConditionType TETRA_TOOL_LEVEL = new LootConditionType(new TetraToolLevelCondition.Serializer());
}
