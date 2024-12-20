package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.google.gson.JsonElement;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerAdvancementManager.class)
public class MixinServerAdvancementManager {

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    public void nukeAllAdvancements(
        Map<ResourceLocation, JsonElement> p_136034_,
        ResourceManager p_136035_,
        ProfilerFiller p_136036_,
        CallbackInfo ci
    ) {
        ci.cancel();
    }
}
