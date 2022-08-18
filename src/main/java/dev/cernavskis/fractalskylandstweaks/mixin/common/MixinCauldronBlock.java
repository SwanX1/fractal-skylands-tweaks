package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import dev.cernavskis.fractalskylandstweaks.util.NBTUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public class MixinCauldronBlock {
  @Inject(method = "entityInside(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
  private void updateFlourItemEntity(BlockState blockState, World world, BlockPos blockPos, Entity entity, CallbackInfo ci) {
    if (entity instanceof ItemEntity) {
      ItemEntity itemEntity = (ItemEntity) entity;
      if (itemEntity.getItem().getItem() == AllItems.WHEAT_FLOUR.get()) {
        CompoundNBT nbt = itemEntity.getPersistentData();
        nbt = NBTUtil.getOrCreateCompound(nbt, "CreateData");
        nbt = NBTUtil.getOrCreateCompound(nbt, "Processing");
        nbt.putString("Type", InWorldProcessing.Type.SPLASHING.name());
        nbt.putInt("Time", 1);
        InWorldProcessing.applyProcessing(itemEntity, InWorldProcessing.Type.SPLASHING);
      }
    }
  }
}
