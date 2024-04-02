// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.integrations.create;

import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import dev.cernavskis.fractalskylandstweaks.mixin.common.KineticBlockEntityAccessor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MillstoneBehaviour extends BlockEntityBehaviour {
    public MillstoneBehaviour(MillstoneBlockEntity tileEntity) {
        super(tileEntity);
    }

    @Override
    public BehaviourType<MillstoneBehaviour> getType() {
        return new BehaviourType<>("millstone_no_energy_behaviour");
    }

    @SuppressWarnings("resource")
    @Override
    public void tick() {
        MillstoneBlockEntity millstone = (MillstoneBlockEntity) this.blockEntity;
        ((KineticBlockEntityAccessor) millstone).setValidationCountdown(Integer.MAX_VALUE);
        super.tick();
        millstone.setSpeed(24.0F);

        if (!this.blockEntity.getLevel().isClientSide) {
            // Spit items instead of inserting them into the output inventory
            for (int slot = 0; slot < millstone.outputInv.getSlots(); slot++) {
                ItemStack stack = millstone.outputInv.extractItem(slot, millstone.outputInv.getSlotLimit(slot), false);
                if (stack.isEmpty()) continue;
                Level level = this.getWorld();
                Vec3 pos = millstone.getBlockPos().getCenter();
    
                double x = pos.x;
                double y = pos.y + 0.5;
                double z = pos.z;
                
                ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.copy());
                itemEntity.setDeltaMovement(0, 0.23, 0);
                level.addFreshEntity(itemEntity);
            }
        }

    }
}
