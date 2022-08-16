package dev.cernavskis.fractalskylandstweaks.tileentity;

import com.simibubi.create.content.contraptions.components.millstone.MillstoneTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;
import dev.cernavskis.fractalskylandstweaks.mixin.common.KineticTileEntityAccessor;

public class MillstoneBehaviour extends TileEntityBehaviour {
    public MillstoneBehaviour(MillstoneTileEntity tileEntity) {
        super(tileEntity);
    }

    @Override
    public BehaviourType<MillstoneBehaviour> getType() {
        return new BehaviourType<>("millstone_no_energy_behaviour");
    }

    @Override
    public void tick() {
        MillstoneTileEntity millstone = (MillstoneTileEntity) this.tileEntity;
        ((KineticTileEntityAccessor) millstone).setValidationCountdown(Integer.MAX_VALUE);
        super.tick();
        millstone.setSpeed(24.0F);
    }
}
