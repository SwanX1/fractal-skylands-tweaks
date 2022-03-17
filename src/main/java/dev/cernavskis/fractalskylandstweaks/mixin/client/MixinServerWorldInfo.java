package dev.cernavskis.fractalskylandstweaks.mixin.client;

import com.mojang.serialization.Lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.storage.ServerWorldInfo;

@Mixin(ServerWorldInfo.class)
public class MixinServerWorldInfo {

  @Inject(
    method = "worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;",
    at = @At("HEAD"),
    cancellable = true
  )
  private void forceStableLifeCycle(CallbackInfoReturnable<Lifecycle> ci) {
    ci.setReturnValue(Lifecycle.stable());
  }
}
