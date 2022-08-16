package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KineticTileEntity.class, remap = false)
public interface KineticTileEntityAccessor {
    @Accessor(value = "validationCountdown")
    int getValidationCountdown();

    @Accessor(value = "validationCountdown")
    void setValidationCountdown(int value);
}
