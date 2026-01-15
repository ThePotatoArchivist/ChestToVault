package archives.tater.chesttovault.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.level.block.VaultBlock;

@Mixin(VaultBlock.class)
public class VaultBlockMixin {
    @ModifyExpressionValue(
            method = "useItemOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z")
    )
    private boolean allowEmptyKey(boolean original) {
        return false;
    }
}
