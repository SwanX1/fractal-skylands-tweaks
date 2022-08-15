package dev.cernavskis.fractalskylandstweaks.data.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.cernavskis.fractalskylandstweaks.data.FSTLootConditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.ToolType;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;

import javax.annotation.CheckForNull;
import java.util.function.BiPredicate;

public class TetraToolLevelCondition implements ILootCondition {
    protected final int level;
    protected final boolean mustBeDoubleHeaded;
    protected final String toolType;
    protected final CompareType compareType;

    private ToolType cachedToolType;

    public TetraToolLevelCondition(int level, boolean mustBeDoubleHeaded, String toolType, CompareType compareType) {
        this.level = level;
        this.mustBeDoubleHeaded = mustBeDoubleHeaded;
        this.toolType = toolType;
        this.compareType = compareType;
    }

    @Override
    public LootConditionType getType() {
        return FSTLootConditions.TETRA_TOOL_LEVEL;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack tool = lootContext.getParamOrNull(LootParameters.TOOL);
        if (tool != null) {
            Item item = tool.getItem();
            if (item instanceof ItemModularHandheld) {
                ItemModularHandheld modularItem = (ItemModularHandheld) item;
                if (this.mustBeDoubleHeaded && !(item instanceof ModularDoubleHeadedItem)) {
                    return false;
                }

                ToolType requiredToolType = this.getToolType();
                if (requiredToolType == null) {
                    for (ToolType toolType : tool.getToolTypes()) {
                        if (this.compare(modularItem.getToolLevel(tool, toolType), this.level)) {
                            return true;
                        }
                    }
                } else {
                    return this.compare(modularItem.getToolLevel(tool, requiredToolType), this.level);
                }
            }
        }
        return false;
    }

    private boolean compare(int a, int b) {
        return this.compareType.compareNumbers(a, b);
    }

    @CheckForNull
    protected ToolType getToolType() {
        if (this.cachedToolType == null && !"any".equals(this.toolType)) {
            this.cachedToolType = ToolType.get(this.toolType);
        }
        return this.cachedToolType;
    }

    public static class Serializer implements ILootSerializer<TetraToolLevelCondition> {

        @Override
        public void serialize(JsonObject jsonObject, TetraToolLevelCondition condition, JsonSerializationContext context) {
            jsonObject.addProperty("level", condition.level);
            jsonObject.addProperty("compare_type", condition.compareType.name);
            jsonObject.addProperty("must_be_double_headed", condition.mustBeDoubleHeaded);
            jsonObject.addProperty("tool_type", condition.toolType);
        }

        @Override
        public TetraToolLevelCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            int level = jsonObject.get("level").getAsInt();
            CompareType compareType = null;
            if (jsonObject.get("compare_type") != null) {
                compareType = CompareType.getByName(jsonObject.get("compare_type").getAsString());
            }
            if (compareType == null) {
                compareType = CompareType.GREATER_OR_EQUAL;
            }

            boolean mustBeDoubleHeaded = jsonObject.get("must_be_double_headed") != null && jsonObject.get("must_be_double_headed").getAsBoolean();
            String toolType = jsonObject.get("tool_type") == null ? "any" : jsonObject.get("tool_type").getAsString();
            return new TetraToolLevelCondition(level, mustBeDoubleHeaded, toolType, compareType);
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
