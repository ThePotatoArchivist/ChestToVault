package archives.tater.chesttovault.datagen;

import archives.tater.chesttovault.ChestToVault;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class StructureTagGenerator extends FabricTagProvider<Structure> {
    public StructureTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.STRUCTURE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ChestToVault.NO_VAULT_REPLACE).add(
                BuiltinStructures.TRIAL_CHAMBERS
        );
    }
}
