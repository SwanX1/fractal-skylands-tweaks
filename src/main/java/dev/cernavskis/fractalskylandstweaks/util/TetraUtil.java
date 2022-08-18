package dev.cernavskis.fractalskylandstweaks.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;

import javax.annotation.CheckForNull;

public final class TetraUtil {
  private TetraUtil() {} // Uninstantiable

  @CheckForNull
  public static Item getVanillaTool(ItemStack stack) {
    Item stackItem = stack.getItem();
    CompoundNBT tag = stack.getTag();
    if (stackItem instanceof ModularSingleHeadedItem) {
      if (tag.getString("single/handle").equals("single/basic_handle") &&
          tag.getString("single/basic_handle_material").equals("basic_handle/stick") &&
          tag.getString("single/head").equals("single/basic_shovel") &&
          tag.getString("single/basic_shovel_material").equals("basic_shovel/oak")) {
        return Items.WOODEN_SHOVEL;
      }
    } else if (stackItem instanceof ModularDoubleHeadedItem) {
      if (tag.getString("double/handle").equals("double/basic_handle") &&
          tag.getString("double/basic_handle_material").equals("basic_handle/stick") &&
          tag.getString("double/head_left").equals("double/basic_pickaxe_left") &&
          tag.getString("double/basic_pickaxe_left_material").equals("basic_pickaxe/oak") &&
          tag.getString("double/head_right").equals("double/basic_pickaxe_right") &&
          tag.getString("double/basic_pickaxe_right_material").equals("basic_pickaxe/oak")) {
        return Items.WOODEN_PICKAXE;
      }

      if (tag.getString("double/handle").equals("double/basic_handle") &&
          tag.getString("double/basic_handle_material").equals("basic_handle/stick") &&
          tag.getString("double/head_left").equals("double/basic_axe_left") &&
          tag.getString("double/basic_axe_left_material").equals("basic_axe/oak") &&
          tag.getString("double/head_right").equals("double/butt_right") &&
          tag.getString("double/butt_right_material").equals("butt/oak")) {
        return Items.WOODEN_AXE;
      }

      if (tag.getString("double/handle").equals("double/basic_handle") &&
          tag.getString("double/basic_handle_material").equals("basic_handle/stick") &&
          tag.getString("double/head_left").equals("double/hoe_left") &&
          tag.getString("double/hoe_left_material").equals("hoe/oak") &&
          tag.getString("double/head_right").equals("double/butt_right") &&
          tag.getString("double/butt_right_material").equals("butt/oak")) {
        return Items.WOODEN_HOE;
      }
    }
    return null;
  }
}
