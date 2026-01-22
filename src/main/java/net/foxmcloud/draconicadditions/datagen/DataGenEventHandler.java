package net.foxmcloud.draconicadditions.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.integration.DACuriosIntegration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber (bus = EventBusSubscriber.Bus.MOD)
public class DataGenEventHandler {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();

		if (event.includeClient()) {
			gen.addProvider(true, new LangGenerator(gen.getPackOutput()));
			gen.addProvider(true, new BlockStateGenerator(gen, event.getExistingFileHelper()));
			gen.addProvider(true, new ItemModelGenerator(gen, event.getExistingFileHelper()));
		}

		if (event.includeServer()) {
			gen.addProvider(true, new RecipeGenerator(event.getLookupProvider(), gen.getPackOutput()));
			gen.addProvider(event.includeServer(), new LootTableProvider(event.getGenerator().getPackOutput(), Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockLootProvider::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
			BlockTagGenerator blockGenerator = new BlockTagGenerator(gen.getPackOutput(), event.getLookupProvider(), DraconicAdditions.MODID, event.getExistingFileHelper());
			gen.addProvider(true, blockGenerator);
			gen.addProvider(true, new ItemTagGenerator(gen.getPackOutput(), event.getLookupProvider(), blockGenerator.contentsGetter(), DraconicAdditions.MODID, event.getExistingFileHelper()));
		}
	}

	private static class ItemTagGenerator extends ItemTagsProvider {
		public ItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider pProvider) {
			if (LoadingModList.get().getModFileById("curios") != null) {
				DACuriosIntegration.generateTags(this::tag);
			}
		}
	}
}
