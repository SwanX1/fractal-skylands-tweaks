// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.core.event;

import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import dev.cernavskis.fractalskylandstweaks.integrations.create.MillstoneBehaviour;
import dev.cernavskis.fractalskylandstweaks.mixin.common.AccessorServerGamePacketListenerImpl;
import dev.cernavskis.fractalskylandstweaks.mixin.common.AccessorServerPlayer;
import dev.cernavskis.fractalskylandstweaks.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEvents extends EventListener {

    @Override
    public void onModInitialization(IEventBus eventBus) {
        super.onModInitialization(eventBus);
        eventBus.addGenericListener(MillstoneBlockEntity.class, this::attachMillstoneBehaviours);
    }

    public void attachMillstoneBehaviours(BlockEntityBehaviourEvent<MillstoneBlockEntity> event) {
        event.attach(new MillstoneBehaviour(event.getBlockEntity()));
    }

    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event) {
        RegistryUtil.setServer(event.getServer());
    }

    @SubscribeEvent
    public void onCreateWorldSpawn(LevelEvent.CreateSpawnPosition event) {
        if (event.getLevel() instanceof ServerLevel level) {
            if (level.dimension() == Level.OVERWORLD) {
                BlockPos worldSpawn = level.getSharedSpawnPos();
                level.setDefaultSpawnPos(worldSpawn.atY(80), 0);
            }
        }
    }

    private static final int DISTANCE_BELOW_VOID = 32;
    private static final int TELEPORT_Y = 384;
    private static final String VOID_FALLING_TAG = "SavedFromVoid";

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer)) return;

        ServerPlayer player = (ServerPlayer) event.player;
        int yBound = player.level().getMinBuildHeight() - DISTANCE_BELOW_VOID;
        CompoundTag persistentData = player.getPersistentData();

        if (
            player.getY() < yBound &&
            player.yo < yBound &&
            ((AccessorServerGamePacketListenerImpl) player.connection).getAwaitingPositionFromClient() == null
        ) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 3));

            if (player.isVehicle()) {
                player.ejectPassengers();
            }

            player.stopRiding();

            ((AccessorServerPlayer) player).setIsChangingDimension(true);
            player.teleportTo(player.getX(), TELEPORT_Y, player.getZ());
            persistentData.putBoolean(VOID_FALLING_TAG, true);
            return;
        }

        if (persistentData.getBoolean(VOID_FALLING_TAG) && (player.isInWater() || player.getAbilities().flying)) {
            persistentData.putBoolean(VOID_FALLING_TAG, false);
            ((AccessorServerPlayer) player).setIsChangingDimension(false);
            return;
        }
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof ServerPlayer player) {
            CompoundTag persistentData = player.getPersistentData();
            if (persistentData.getBoolean(VOID_FALLING_TAG)) {
                float damage = 20;
                if (player.getHealth() - damage <= 0) {
                    damage = player.getHealth() - 1f;
                }

                event.setDamageMultiplier(0);
                ((AccessorServerPlayer) player).setIsChangingDimension(false);
                player.hurt(player.level().damageSources().fall(), damage);
            }
        }
    }
}
