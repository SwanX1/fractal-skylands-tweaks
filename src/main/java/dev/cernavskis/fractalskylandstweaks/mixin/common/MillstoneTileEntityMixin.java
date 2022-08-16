package dev.cernavskis.fractalskylandstweaks.mixin.common;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.components.millstone.MillstoneTileEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MillstoneTileEntity.class, remap = false)
public class MillstoneTileEntityMixin extends KineticTileEntity {
    private MillstoneTileEntityMixin(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Inject(method = "lambda$process$1(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void spitItem(ItemStack stack, CallbackInfo ci) {
        World world = this.getWorld();
        BlockPos pos = this.worldPosition.immutable();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        ItemEntity itemEntity = new ItemEntity(world, x + 0.5, y + 1, z + 0.5, stack.copy());
        itemEntity.setDeltaMovement(0, 0.23, 0);
        world.addFreshEntity(itemEntity);
        ci.cancel();
    }
}
