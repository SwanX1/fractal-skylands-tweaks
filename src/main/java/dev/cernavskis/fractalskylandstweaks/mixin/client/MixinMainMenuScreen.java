package dev.cernavskis.fractalskylandstweaks.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.gui.screen.MainMenuScreen;

@Mixin(MainMenuScreen.class)
public class MixinMainMenuScreen {
  @Unique
  private boolean allowsMultiplayer;

  @ModifyVariable(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;addButton(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 2, shift = Shift.BEFORE), ordinal = 0)
  private boolean setFlag(boolean in) {
    this.allowsMultiplayer = in;
    return false;
  }

  @ModifyVariable(method = "createNormalMenuOptions", at = @At("TAIL"), ordinal = 0)
  private boolean setFlagBack(boolean in) {
    return this.allowsMultiplayer;
  }
}
