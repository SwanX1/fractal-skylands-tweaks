package dev.cernavskis.fractalskylandstweaks.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public interface AccessorServerPlayer {
  @Accessor("isChangingDimension")
  public void setIsChangingDimension(boolean value);
}
