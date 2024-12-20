// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.client;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import java.util.Optional;
import java.util.OptionalLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyWorldType(
        Minecraft p_276053_,
        Screen p_276049_,
        WorldCreationContext worldCreationContext,
        Optional<ResourceKey<WorldPreset>> worldPreset,
        OptionalLong p_276031_,
        CallbackInfo ci
    ) {
        WorldCreationUiState uiState = ((CreateWorldScreen) (Object) this).getUiState();
        uiState.setWorldType(
            new WorldCreationUiState.WorldTypeEntry(
                (Holder<WorldPreset>) worldCreationContext
                    .worldgenLoadContext()
                    .registryOrThrow(Registries.WORLD_PRESET)
                    .getHolder(ResourceKey.create(Registries.WORLD_PRESET, new ResourceLocation(FractalSkylandsTweaks.MOD_ID, "islands")))
                    .orElseThrow()
            )
        );
    }
}
