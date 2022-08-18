package dev.cernavskis.fractalskylandstweaks.mixin.common;

import dev.cernavskis.fractalskylandstweaks.util.TetraUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe {
  @ModifyArg(
      method = "matches(Lnet/minecraft/inventory/CraftingInventory;IIZ)Z",
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/item/crafting/Ingredient.test(Lnet/minecraft/item/ItemStack;)Z",
          ordinal = 0
      ),
      index = 0
  )
  private ItemStack replaceIngredient(ItemStack stack) {
    Item item = stack.getItem();
    if (item instanceof ItemModularHandheld) {
      ItemStack replacement = new ItemStack(TetraUtil.getVanillaTool(stack));
      if (replacement != null) {
        return replacement;
      }
    }
    return stack;
  }

}
