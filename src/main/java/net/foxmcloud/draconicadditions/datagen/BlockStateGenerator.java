package net.foxmcloud.draconicadditions.datagen;

import java.util.function.Function;

import com.brandon3055.draconicevolution.DraconicEvolution;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created by brandon3055 on 28/2/20.
 */
public class BlockStateGenerator extends BlockStateProvider {

	public BlockStateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, DraconicAdditions.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		ResourceLocation machineTop = new ResourceLocation(DraconicEvolution.MODID, "block/parts/machine_top");
		simpleBlock(DAContent.chaosLiquefier, models().cubeBottomTop("chaos_liquefier", modLoc("blocks/chaos_liquefier_front"), machineTop, machineTop));
	}

	private void dummyBlock(Block block) {
		ModelFile model = models()//
				.withExistingParent("dummy", "block")//
				.texture("particle", "minecraft:block/glass");
		simpleBlock(block, model);
	}

	public void directionalFromNorth(Block block, ModelFile model) {
		directionalFromNorth(block, model, 180);
	}

	public void directionalFromNorth(Block block, ModelFile model, int angleOffset) {
		directionalFromNorth(block, $ -> model, angleOffset);
	}

	public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc) {
		directionalFromNorth(block, modelFunc, 180);
	}

	public void directionalFromNorth(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		getVariantBuilder(block)
		.forAllStates(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder()
					.modelFile(modelFunc.apply(state))
					.rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? -90 : 0)
					.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
					.build();
		});
	}

	@Override
	public String getName() {
		return "Draconic Additions Blockstates";
	}
}
