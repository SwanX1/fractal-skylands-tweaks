// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.mixin.common;

import dev.cernavskis.fractalskylandstweaks.util.TetraUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe {

    @ModifyArg(
        method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;IIZ)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z",
            ordinal = 0
        ),
        index = 0
    )
    private ItemStack replaceIngredient(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemModularHandheld) {
            Item vanilla = TetraUtil.getVanillaTool(stack);
            if (vanilla != null) {
                return new ItemStack(vanilla);
            }
        }
        return stack;
    }
}
