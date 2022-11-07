package net.foxmcloud.draconicadditions.datagen;

import com.brandon3055.draconicevolution.api.modules.Module;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Created by brandon3055 on 28/2/20.
 */
public class ItemModelGenerator extends ItemModelProvider {

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, DraconicAdditions.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		blockItem(DAContent.chaosLiquefier);
		blockItem(DAContent.chaosInfuser);
		simpleItem(DAContent.chaosHeart);
		simpleItem(DAContent.inertPotatoHelm);
		simpleItem(DAContent.inertPotatoChest);
		simpleItem(DAContent.inertPotatoLegs);
		simpleItem(DAContent.inertPotatoBoots);
		simpleArmor(DAContent.infusedPotatoHelm);
		simpleArmor(DAContent.infusedPotatoChest);
		simpleArmor(DAContent.infusedPotatoLegs);
		simpleArmor(DAContent.infusedPotatoBoots);
		simpleItem(DAContent.chaosContainer, "items/tools/animated");
		simpleCurios(DAContent.necklaceWyvern);
		simpleCurios(DAContent.necklaceDraconic);
		simpleCurios(DAContent.necklaceChaotic);
		simpleCurios(DAContent.harnessWyvern);
		simpleCurios(DAContent.harnessDraconic);
		simpleCurios(DAContent.harnessChaotic);
		simpleItem(DAContent.hermal, new ResourceLocation("minecraft", "item/poisonous_potato"));
		simpleModule(DAModules.chaoticAutoFeed);
		simpleModule(DAModules.draconicTickAccel);
		simpleModule(DAModules.chaoticTickAccel);
		simpleModule(DAModules.semiStableChaos);
		simpleModule(DAModules.stableChaos);
		simpleModule(DAModules.unstableChaos);
		simpleModule(DAModules.chaosInjector);
	}

	private void simpleItem(Item item) {
		simpleItem(item, "items/crafting");
	}

	private void simpleArmor(Item item) {
		simpleItem(item, "items/armor");
	}

	private void simpleCurios(Item item) {
		simpleItem(item, "items/curios");
	}

	private void simpleItem(Item item, String textureFolder) {
		if (item == null) return;
		ResourceLocation reg = item.getRegistryName();
		simpleItem(item, new ResourceLocation(reg.getNamespace(), textureFolder + "/" + reg.getPath()));
	}

	private void simpleItem(Item item, ResourceLocation texture) {
		if (item == null) return;
		ResourceLocation reg = item.getRegistryName();
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture);
	}

	private void simpleModule(Module<?> module) {
		simpleModule(module, "items/modules");
	}

	private void simpleModule(Module<?> module, String textureFolder) {
		if (module == null || module.getItem() == null) return;
		ResourceLocation reg = module.getItem().getRegistryName();
		simpleItem(module.getItem(), new ResourceLocation(reg.getNamespace(), textureFolder + "/" + reg.getPath().replace("_module", "")));
	}

	private void blockItem(Block block) {
		if (block == null) return;
		ResourceLocation reg = block.getRegistryName();
		blockItem(block, new ResourceLocation(reg.getNamespace(), "block/" + reg.getPath()));
	}

	private void blockItem(Block block, ResourceLocation blockModel) {
		if (block == null) return;
		ResourceLocation reg = block.getRegistryName();
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile(blockModel));
	}

	private void dummyModel(Block block) {
		dummyModel(block.asItem());
	}

	private void dummyModel(Item item) {
		getBuilder(item.getRegistryName().getPath())
		.parent(new ModelFile.UncheckedModelFile("builtin/generated"));
	}

	@Override
	public String getName() {
		return "Draconic Additions Item Models";
	}
}
