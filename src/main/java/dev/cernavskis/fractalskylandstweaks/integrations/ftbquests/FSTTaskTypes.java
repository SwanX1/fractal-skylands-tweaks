package dev.cernavskis.fractalskylandstweaks.integrations.ftbquests;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.minecraft.resources.ResourceLocation;

public class FSTTaskTypes {

    public static final TaskType TETRA_TOOL_ITEM = TaskTypes.register(
        new ResourceLocation(FractalSkylandsTweaks.MOD_ID, "tetra_tool_item"),
        TetraToolItemTask::new,
        () -> Icon.getIcon(new ResourceLocation(FractalSkylandsTweaks.MOD_ID, "textures/icon/iron_hammer.png"))
    );

    public static void classload() {
        FractalSkylandsTweaks.getLogger().info("Classloading FTBQuest custom tasks...");
    }
}
