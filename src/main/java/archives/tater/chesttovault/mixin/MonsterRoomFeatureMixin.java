package archives.tater.chesttovault.mixin;

import archives.tater.chesttovault.ChestToVault;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.storage.loot.LootTable;

@Mixin(MonsterRoomFeature.class)
public class MonsterRoomFeatureMixin {
    @ModifyExpressionValue(
            method = "place",
            at = @At(value = "FIELD:LAST", target = "Lnet/minecraft/world/level/block/Blocks;CHEST:Lnet/minecraft/world/level/block/Block;")
    )
    private Block placeVault(Block original) {
        return Blocks.VAULT;
    }

    @WrapOperation(
            method = "place",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/RandomizableContainer;setBlockEntityLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceKey;)V")
    )
    private void setVaultLoot(BlockGetter level, RandomSource random, BlockPos ps, ResourceKey<LootTable> lootTable, Operation<Void> original) {
        ChestToVault.setLootTable(level.getBlockEntity(ps), lootTable);
    }
}
