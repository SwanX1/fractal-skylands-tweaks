// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Block.class)
public class MixinBlock {
  @ModifyArg(
    method = "popResource(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/World;addFreshEntity(Lnet/minecraft/entity/Entity;)Z",
      ordinal = 0
    ),
    index = 0
  )
  private static Entity setImmediatePickUp(Entity e) {
    if (e instanceof ItemEntity) {
      ItemEntity item = (ItemEntity) e;
      item.setNoPickUpDelay();
      return item;
    } else {
      return e;
    }
  }
}
