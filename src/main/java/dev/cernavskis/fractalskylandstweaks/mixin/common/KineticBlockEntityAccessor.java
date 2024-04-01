// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

@Mixin(value = KineticBlockEntity.class, remap = false)
public interface KineticBlockEntityAccessor {
    @Accessor(value = "validationCountdown")
    int getValidationCountdown();

    @Accessor(value = "validationCountdown")
    void setValidationCountdown(int value);
}
