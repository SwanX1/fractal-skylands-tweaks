// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KineticBlockEntity.class, remap = false)
public interface AccessorKineticBlockEntity {
    @Accessor(value = "validationCountdown")
    int getValidationCountdown();

    @Accessor(value = "validationCountdown")
    void setValidationCountdown(int value);
}
