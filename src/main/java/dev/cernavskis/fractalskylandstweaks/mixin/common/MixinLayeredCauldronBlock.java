// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessing;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import dev.cernavskis.fractalskylandstweaks.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public class MixinLayeredCauldronBlock {

    @Inject(method = "entityInside", at = @At("TAIL"))
    public void updateFlourItemEntity(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo info) {
        if (!level.isClientSide && entity instanceof ItemEntity itemEntity) {
            if (itemEntity.getItem().getItem() == AllItems.WHEAT_FLOUR.get()) {
                CompoundTag nbt = itemEntity.getPersistentData();
                nbt = NBTUtil.getOrCreateCompound(nbt, "CreateData");
                nbt = NBTUtil.getOrCreateCompound(nbt, "Processing");
                nbt.putString("Type", FanProcessingTypeRegistry.getId(AllFanProcessingTypes.SPLASHING).toString());
                nbt.putInt("Time", 1);
                FanProcessing.applyProcessing(itemEntity, AllFanProcessingTypes.SPLASHING);
            }
        }
    }
}
