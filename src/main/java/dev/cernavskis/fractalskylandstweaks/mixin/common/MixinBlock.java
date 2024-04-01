// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;

@Mixin(Block.class)
public class MixinBlock {
  @ModifyArg(
    method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
      ordinal = 0
    ),
    index = 0
  )
  private static Entity setImmediatePickUp(Entity e) {
    ItemEntity item = (ItemEntity) e;
    item.setNoPickUpDelay();
    return item;
  }
}
