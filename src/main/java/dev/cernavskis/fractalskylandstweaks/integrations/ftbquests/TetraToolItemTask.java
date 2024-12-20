package dev.cernavskis.fractalskylandstweaks.integrations.ftbquests;

import dev.cernavskis.fractalskylandstweaks.integrations.ftbquests.util.TetraToolTaskConfig;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.items.modular.ModularItem;

public class TetraToolItemTask extends Task {

    private TetraToolTaskConfig taskConfig = new TetraToolTaskConfig();

    public TetraToolItemTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return FSTTaskTypes.TETRA_TOOL_ITEM;
    }

    @Override
    public long getMaxProgress() {
        return 1L;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        this.taskConfig.writeData(nbt);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.taskConfig = new TetraToolTaskConfig();
        this.taskConfig.readData(nbt);
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        this.taskConfig.writeNetData(buffer);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.taskConfig = new TetraToolTaskConfig();
        this.taskConfig.readNetData(buffer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.fractalskylandstweaks.tetra_tool_item");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);

        this.taskConfig.fillConfigGroup(config);
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!teamData.isCompleted(this) && this.checkTaskSequence(teamData)) {
            if (this.test(craftedItem) || player.getInventory().items.stream().anyMatch(stack -> this.test(stack))) {
                teamData.addProgress(this, 1L);
                return;
            }
        }
    }

    @Override
    public boolean submitItemsOnInventoryChange() {
        return true;
    }

    private boolean test(ItemStack stack) {
        return stack.getItem() instanceof ModularItem modular && this.taskConfig.test(modular, stack);
    }
}
