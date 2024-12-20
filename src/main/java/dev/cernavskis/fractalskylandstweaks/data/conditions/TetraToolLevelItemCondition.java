// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.data.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.cernavskis.fractalskylandstweaks.core.FSTLootConditions;
import java.util.function.BiPredicate;
import javax.annotation.CheckForNull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;

public class TetraToolLevelItemCondition implements LootItemCondition {

    protected final int level;
    protected final boolean mustBeDoubleHeaded;
    protected final String toolType;
    protected final CompareType compareType;
    private ToolAction cachedToolType;

    public TetraToolLevelItemCondition(int level, boolean mustBeDoubleHeaded, String toolType, CompareType compareType) {
        this.level = level;
        this.mustBeDoubleHeaded = mustBeDoubleHeaded;
        this.toolType = toolType;
        this.compareType = compareType;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack tool = lootContext.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            Item item = tool.getItem();
            if (item instanceof ItemModularHandheld modularItem) {
                if (this.mustBeDoubleHeaded && !(item instanceof ModularDoubleHeadedItem)) {
                    return false;
                }

                ToolAction requiredToolAction = this.getToolAction();
                if (requiredToolAction == null) {
                    for (ToolAction toolType : modularItem.getToolActions(tool)) {
                        if (this.compare(modularItem.getToolLevel(tool, toolType), this.level)) {
                            return true;
                        }
                    }
                } else {
                    return this.compare(modularItem.getToolLevel(tool, requiredToolAction), this.level);
                }
            }
        }
        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return FSTLootConditions.TETRA_TOOL_LEVEL;
    }

    private boolean compare(int a, int b) {
        return this.compareType.compareNumbers(a, b);
    }

    @CheckForNull
    protected ToolAction getToolAction() {
        if (this.cachedToolType == null && !"any".equals(this.toolType)) {
            this.cachedToolType = ToolAction.get(this.toolType);
        }
        return this.cachedToolType;
    }

    public static class TSerializer implements Serializer<TetraToolLevelItemCondition> {

        @Override
        public void serialize(JsonObject jsonObject, TetraToolLevelItemCondition condition, JsonSerializationContext context) {
            jsonObject.addProperty("level", condition.level);
            jsonObject.addProperty("compare_type", condition.compareType.name);
            jsonObject.addProperty("must_be_double_headed", condition.mustBeDoubleHeaded);
            jsonObject.addProperty("tool_type", condition.toolType);
        }

        @Override
        public TetraToolLevelItemCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            int level = jsonObject.get("level").getAsInt();
            CompareType compareType = null;
            if (jsonObject.get("compare_type") != null) {
                compareType = CompareType.getByName(jsonObject.get("compare_type").getAsString());
            }
            if (compareType == null) {
                compareType = CompareType.GREATER_OR_EQUAL;
            }

            boolean mustBeDoubleHeaded =
                jsonObject.get("must_be_double_headed") != null && jsonObject.get("must_be_double_headed").getAsBoolean();
            String toolType = jsonObject.get("tool_type") == null ? "any" : jsonObject.get("tool_type").getAsString();
            return new TetraToolLevelItemCondition(level, mustBeDoubleHeaded, toolType, compareType);
        }
    }

    public static enum CompareType {
        LESS("less", (a, b) -> a.doubleValue() < b.doubleValue()),
        LESS_OR_EQUAL("less_or_equal", (a, b) -> a.doubleValue() <= b.doubleValue()),
        GREATER("greater", (a, b) -> a.doubleValue() > b.doubleValue()),
        GREATER_OR_EQUAL("greater_or_equal", (a, b) -> a.doubleValue() >= b.doubleValue()),
        EQUALS("equals", (a, b) -> a.doubleValue() == b.doubleValue());

        private final String name;
        private final BiPredicate<Number, Number> predicate;

        CompareType(String name, BiPredicate<Number, Number> predicate) {
            this.name = name;
            this.predicate = predicate;
        }

        public boolean compareNumbers(Number a, Number b) {
            return this.predicate.test(a, b);
        }

        @CheckForNull
        public static CompareType getByName(String name) {
            for (CompareType compareType : CompareType.values()) {
                if (compareType.name.equals(name)) {
                    return compareType;
                }
            }
            return null;
        }
    }
}
