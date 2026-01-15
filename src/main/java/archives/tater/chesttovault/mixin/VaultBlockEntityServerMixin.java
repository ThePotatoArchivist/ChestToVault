package archives.tater.chesttovault.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;

@Mixin(VaultBlockEntity.Server.class)
public class VaultBlockEntityServerMixin {
    @ModifyReturnValue(
            method = "isValidToInsert",
            at = @At("RETURN")
    )
    private static boolean allowEmptyKey(boolean original, @Local(argsOnly = true) VaultConfig config) {
        return original || config.keyItem().isEmpty();
    }

    @ModifyExpressionValue(
            method = "canEjectReward",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z")
    )
    private static boolean allowEmptyKey(boolean original) {
        return false;
    }
}
