package dev.cernavskis.fractalskylandstweaks.integrations.ftbquests.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dev.cernavskis.fractalskylandstweaks.util.NBTUtil;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ModuleRegistry;
import se.mickelus.tetra.module.data.VariantData;

public class TetraToolTaskConfig {

    public ModularItemType modularItemType = ModularItemType.ANY;

    // Slot -> (Module, Variants)
    // If module is not present, but the slot key exists, it is assumed that the module and variands are not required,
    // If the slot key does not exist, don't check it
    // If the module is present, but the variants list is empty, any variant is accepted
    // If the module is present, and the variants list is not empty, any of the the specified variants are accepted

    // JSON representation:
    // {
    //  "type": "tetra_tool_item",
    //  "modular_item": "bladed",
    //  "required_modules": {
    //    "slot_key": {
    //      "module": "module_key",
    //      "variants": ["variant_key", "variant_key"]
    //    }
    //  },
    //  "required_tool_level": {
    //    "some_action": 1
    //  }
    // }
    public final Map<String, Pair<ItemModule, List<String>>> requiredModules = new HashMap<>();
    public final Map<ToolAction, Integer> requiredToolLevel = new HashMap<>();

    public void addRequiredSlot(String slot) {
        this.addRequiredModule(slot, null);
    }

    public void addRequiredModule(String slot, ItemModule module) {
        this.requiredModules.put(slot, Pair.of(module, new ArrayList<>()));
    }

    public void addRequiredVariant(String slot, ItemModule module, String variant) {
        Pair<ItemModule, List<String>> pair = this.requiredModules.get(slot);

        if (pair != null) {
            pair.getSecond().add(variant);
        }
    }

    public void setRequiredToolLevel(ToolAction action, int level) {
        this.requiredToolLevel.put(action, level);
    }

    public void writeData(CompoundTag nbt) {
        nbt.putString("modular_item", this.modularItemType.name);

        CompoundTag requiredModulesTag = new CompoundTag();
        for (Map.Entry<String, Pair<ItemModule, List<String>>> entry : this.requiredModules.entrySet()) {
            String slot = entry.getKey();
            Pair<ItemModule, List<String>> pair = entry.getValue();
            ItemModule module = pair.getFirst();
            List<String> variants = pair.getSecond();

            CompoundTag slotTag = new CompoundTag();
            if (module != null) {
                slotTag.putString("module", module.getKey());
                CompoundTag variantsTag = new CompoundTag();
                NBTUtil.putStringArray(variantsTag, variants);
                slotTag.put("variants", variantsTag);
            }

            requiredModulesTag.put(slot, slotTag);
        }

        nbt.put("required_modules", requiredModulesTag);

        CompoundTag requiredToolLevelTag = new CompoundTag();
        for (Map.Entry<ToolAction, Integer> entry : this.requiredToolLevel.entrySet()) {
            ToolAction action = entry.getKey();
            int level = entry.getValue();

            requiredToolLevelTag.putInt(action.name(), level);
        }

        nbt.put("required_tool_level", requiredToolLevelTag);
    }

    public void readData(CompoundTag nbt) {
        this.modularItemType = ModularItemType.fromName(nbt.getString("modular_item"));

        CompoundTag requiredModulesTag = nbt.getCompound("required_modules");
        for (String slot : requiredModulesTag.getAllKeys()) {
            CompoundTag slotTag = requiredModulesTag.getCompound(slot);
            ItemModule module = null;
            List<String> variants = new ArrayList<>();

            if (slotTag.contains("module", 8)) {
                module = ModuleRegistry.instance.getModule(ResourceLocation.tryParse(slotTag.getString("module")));
                variants = NBTUtil.getStringArray(slotTag.getCompound("variants"));
            }

            this.requiredModules.put(slot, Pair.of(module, variants));
        }

        CompoundTag requiredToolLevelTag = nbt.getCompound("required_tool_level");
        for (String action : requiredToolLevelTag.getAllKeys()) {
            this.requiredToolLevel.put(ToolAction.get(action), requiredToolLevelTag.getInt(action));
        }
    }

    public void writeNetData(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.modularItemType.name);

        buffer.writeVarInt(this.requiredModules.size());
        for (Map.Entry<String, Pair<ItemModule, List<String>>> entry : this.requiredModules.entrySet()) {
            buffer.writeUtf(entry.getKey());
            Pair<ItemModule, List<String>> pair = entry.getValue();
            ItemModule module = pair.getFirst();
            List<String> variants = pair.getSecond();

            buffer.writeBoolean(module != null);
            if (module != null) {
                buffer.writeUtf(module.getKey().toString());
                buffer.writeVarInt(variants.size());
                for (String variant : variants) {
                    buffer.writeUtf(variant);
                }
            }
        }

        buffer.writeVarInt(this.requiredToolLevel.size());
        for (Map.Entry<ToolAction, Integer> entry : this.requiredToolLevel.entrySet()) {
            buffer.writeUtf(entry.getKey().name());
            buffer.writeVarInt(entry.getValue());
        }
    }

    public void readNetData(FriendlyByteBuf buffer) {
        this.modularItemType = ModularItemType.fromName(buffer.readUtf());

        int requiredModulesSize = buffer.readVarInt();
        for (int i = 0; i < requiredModulesSize; i++) {
            String slot = buffer.readUtf();
            ItemModule module = null;
            List<String> variants = new ArrayList<>();

            if (buffer.readBoolean()) {
                module = ModuleRegistry.instance.getModule(ResourceLocation.tryParse(buffer.readUtf()));
                int variantsSize = buffer.readVarInt();
                for (int j = 0; j < variantsSize; j++) {
                    variants.add(buffer.readUtf());
                }
            }

            this.requiredModules.put(slot, Pair.of(module, variants));
        }

        int requiredToolLevelSize = buffer.readVarInt();
        for (int i = 0; i < requiredToolLevelSize; i++) {
            this.requiredToolLevel.put(ToolAction.get(buffer.readUtf()), buffer.readVarInt());
        }
    }

    private static final Gson GSON = new Gson();

    private JsonObject requiredModulesAsJson() {
        JsonObject json = new JsonObject();
        for (Map.Entry<String, Pair<ItemModule, List<String>>> entry : this.requiredModules.entrySet()) {
            String slot = entry.getKey();
            Pair<ItemModule, List<String>> pair = entry.getValue();
            ItemModule module = pair.getFirst();
            List<String> variants = pair.getSecond();

            JsonObject slotJson = new JsonObject();
            if (module != null) {
                slotJson.addProperty("module", module.getKey().toString());
                slotJson.add("variants", GSON.toJsonTree(variants));
            }

            json.add(slot, slotJson);
        }

        return json;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Pair<ItemModule, List<String>>> requiredModulesFromJson(JsonObject json) {
        Map<String, Pair<ItemModule, List<String>>> requiredModules = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String slot = entry.getKey();
            JsonObject slotJson = entry.getValue().getAsJsonObject();
            ItemModule module = null;
            List<String> variants = new ArrayList<>();

            if (slotJson.has("module")) {
                module = ModuleRegistry.instance.getModule(ResourceLocation.tryParse(slotJson.get("module").getAsString()));
                variants = GSON.fromJson(slotJson.get("variants"), List.class);
            }

            requiredModules.put(slot, Pair.of(module, variants));
        }

        return requiredModules;
    }

    public void fillConfigGroup(ConfigGroup config) {
        config.addEnum(
            "modular_item",
            this.modularItemType,
            value -> this.modularItemType = value,
            NameMap.of(ModularItemType.ANY, ModularItemType.values()).create()
        );

        // Use JSON strings for this. It's easier to implement.
        config.addString(
            "required_modules",
            GSON.toJson(requiredModulesAsJson()),
            value -> this.requiredModules.putAll(requiredModulesFromJson(GSON.fromJson(value, JsonObject.class))),
            GSON.toJson(requiredModulesAsJson())
        );

        ConfigGroup requiredToolLevelGroup = config.getOrCreateSubgroup("required_tool_level");

        for (ToolAction action : ToolAction.getActions()) {
            requiredToolLevelGroup.addInt(
                action.name(),
                this.requiredToolLevel.getOrDefault(action, 0),
                value -> this.requiredToolLevel.put(action, value),
                this.requiredToolLevel.getOrDefault(action, 0),
                0,
                Integer.MAX_VALUE
            );
        }
    }

    public boolean test(ModularItem modular, ItemStack stack) {
        if (this.modularItemType != null && !this.modularItemType.isInstance(modular)) {
            return false;
        }

        for (Map.Entry<String, Pair<ItemModule, List<String>>> entry : this.requiredModules.entrySet()) {
            String slot = entry.getKey();
            ItemModule requiredModule = entry.getValue().getFirst();
            List<String> requiredVariants = entry.getValue().getSecond();
            ItemModule actualModule = modular.getModuleFromSlot(stack, slot);

            if (actualModule == null || !actualModule.getKey().equals(requiredModule.getKey())) {
                return false;
            }

            for (String requiredVariant : requiredVariants) {
                VariantData variant = actualModule.getVariantData(slot);
                if (variant.key == null || !variant.key.equals(requiredVariant)) {
                    return false;
                }
            }
        }

        for (Map.Entry<ToolAction, Integer> entry : this.requiredToolLevel.entrySet()) {
            ToolAction action = entry.getKey();
            int requiredLevel = entry.getValue();
            int actualLevel = modular.getToolLevel(stack, action);

            if (actualLevel < requiredLevel) {
                return false;
            }
        }

        return true;
    }

    public static enum ModularItemType {
        ANY("any", null),
        BLADED("bladed", ModularBladedItem.class),
        BOW("bow", ModularBowItem.class),
        CROSSBOW("crossbow", ModularCrossbowItem.class),
        DOUBLE_HEADED("double_headed", ModularDoubleHeadedItem.class),
        HOLOSPHERE("holosphere", ModularHolosphereItem.class),
        SHIELD("shield", ModularShieldItem.class),
        SINGLE_HEADED("single_headed", ModularSingleHeadedItem.class),
        TOOLBELT("toolbelt", ModularToolbeltItem.class);

        public final String name;
        private final Class<? extends ModularItem> itemClass;

        private ModularItemType(String name, Class<? extends ModularItem> itemClass) {
            this.name = name;
            this.itemClass = itemClass;
        }

        private boolean isInstance(ModularItem item) {
            return this == ANY || this.itemClass.isInstance(item);
        }

        public static ModularItemType fromName(String item) {
            for (ModularItemType type : values()) {
                if (type.name.equals(item)) {
                    return type;
                }
            }

            return ANY;
        }
    }
}
