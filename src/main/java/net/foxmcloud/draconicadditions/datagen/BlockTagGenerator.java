package net.foxmcloud.draconicadditions.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockTagGenerator extends BlockTagsProvider {

	public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(DAContent.chaosLiquifier.get())
			.add(DAContent.chaosInfuser.get())
			.add(DAContent.chaosExtractor.get())
			.add(DAContent.chaosCrystalizer.get())
			.add(DAContent.fakeReactorCore.get())
			.add(DAContent.fakeReactorInjector.get())
			.add(DAContent.fakeReactorStabilizer.get());
	}
}
