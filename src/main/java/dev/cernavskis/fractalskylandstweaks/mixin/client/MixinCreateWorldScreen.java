package dev.cernavskis.fractalskylandstweaks.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.widget.Widget;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen {
  @ModifyArg(
    method = "init",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/screen/CreateWorldScreen;addButton(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;",
      ordinal = 5
    ),
    index = 0
  )
  private Widget disableMoreWorldOptionsButton(Widget button) {
    button.visible = false;
    return button;
  }
}
