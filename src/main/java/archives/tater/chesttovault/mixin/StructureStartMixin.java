package archives.tater.chesttovault.mixin;

import archives.tater.chesttovault.ChestToVault;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Shadow
    @Final
    private Structure structure;

    @WrapOperation(
            method = "placeInChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;postProcess(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V")
    )
    private void test(StructurePiece instance, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos, Operation<Void> original) {
        original.call(instance, worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, pos);
        var structureHolder = worldGenLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE).wrapAsHolder(structure);
        if (structureHolder.is(ChestToVault.NO_VAULT_REPLACE)) return;
        BlockPos.betweenClosedStream(boundingBox).forEach(checkPos -> {
            var state = worldGenLevel.getBlockState(checkPos);
            if (!state.is(Blocks.CHEST)) return;

            if (!(worldGenLevel.getBlockEntity(checkPos) instanceof ChestBlockEntity chest)) return;

            var lootTable = chest.getLootTable();
            if (lootTable == null) return;

            // Necessary to set the block entity correctly for some reason, mojang does it too
            worldGenLevel.setBlock(checkPos, Blocks.BARRIER.defaultBlockState(), Block.UPDATE_SKIP_ALL_SIDEEFFECTS | Block.UPDATE_INVISIBLE);

            worldGenLevel.setBlock(checkPos, Blocks.VAULT.withPropertiesOf(state), Block.UPDATE_CLIENTS);
            if (!(worldGenLevel.getBlockEntity(checkPos) instanceof VaultBlockEntity vault)) return;

            var config = vault.getConfig();
            vault.setConfig(new VaultConfig(
                    chest.getLootTable(),
                    config.activationRange(),
                    config.deactivationRange(),
                    ItemStack.EMPTY,
                    config.overrideLootTableToDisplay(),
                    config.playerDetector(),
                    config.entitySelector()
            ));
        });
    }
}