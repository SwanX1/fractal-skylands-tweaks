package dev.cernavskis.fractalskylandstweaks.mixin.common;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface AccessorServerGamePacketListenerImpl {
    @Accessor("awaitingPositionFromClient")
    public Vec3 getAwaitingPositionFromClient();
}
