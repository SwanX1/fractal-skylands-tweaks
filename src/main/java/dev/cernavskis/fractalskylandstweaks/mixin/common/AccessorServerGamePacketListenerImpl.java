package dev.cernavskis.fractalskylandstweaks.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;

@Mixin(ServerGamePacketListenerImpl.class)
public interface AccessorServerGamePacketListenerImpl {
  @Accessor("awaitingPositionFromClient")
  public Vec3 getAwaitingPositionFromClient();
}
