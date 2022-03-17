package dev.cernavskis.fractalskylandstweaks.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.WorldOptionsScreen;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;

@Mixin(WorldOptionsScreen.class)
public class MixinWorldOptionsScreen {
  @Shadow
  private DimensionGeneratorSettings settings;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void toggleFeaturesOff(CallbackInfo ci) {
    if (this.settings.generateFeatures()) {
      this.settings = this.settings.withFeaturesToggled();
    }
  }
}
