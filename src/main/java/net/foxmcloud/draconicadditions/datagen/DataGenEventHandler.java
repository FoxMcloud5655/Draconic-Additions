package net.foxmcloud.draconicadditions.datagen;

import javax.annotation.Nullable;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.integration.DACuriosIntegration;
import net.foxmcloud.draconicadditions.world.DADimension;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEventHandler {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		if (event.includeClient()) {
			gen.addProvider(new LangGenerator(gen));
			gen.addProvider(new BlockStateGenerator(gen, event.getExistingFileHelper()));
			gen.addProvider(new ItemModelGenerator(gen, event.getExistingFileHelper()));
		}
		if (event.includeServer()) {
			gen.addProvider(new RecipeGenerator(gen));
			gen.addProvider(new LootTableGenerator(gen));
			BlockTagGenerator blockGenerator = new BlockTagGenerator(gen, DraconicAdditions.MODID, event.getExistingFileHelper());
			gen.addProvider(blockGenerator);
			gen.addProvider(new ItemTagGenerator(gen, blockGenerator, DraconicAdditions.MODID, event.getExistingFileHelper()));
			gen.addProvider(new DimensionCopyProvider(gen, DADimension.HARNESS_DIM.location(), DADimension.HARNESS_DIM_TYPE.location()));
		}
	}

	private static class ItemTagGenerator extends ItemTagsProvider {
		public ItemTagGenerator(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagProvider, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
			if (ModList.get().isLoaded("curios")) {
				DACuriosIntegration.generateTags(this::tag);
			}
		}
	}

	private static class BlockTagGenerator extends BlockTagsProvider {
		public BlockTagGenerator(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper) {
			super(generatorIn, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {}
	}
}
