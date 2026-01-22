package net.foxmcloud.draconicadditions.datagen;

import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleRegistry;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Created by FoxMcloud5655 on 23/11/22.
 */
public class ItemModelGenerator extends ItemModelProvider {

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), DraconicAdditions.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		blockItem(DAContent.chaosLiquifier);
		blockItem(DAContent.chaosInfuser);
		blockItem(DAContent.chaosExtractor);
		blockItem(DAContent.chaosCrystalizer);
		simpleItem(DAContent.chaosHeart);
		simpleItem(DAContent.inertPotatoHelm);
		simpleItem(DAContent.inertPotatoChest);
		simpleItem(DAContent.inertPotatoLegs);
		simpleItem(DAContent.inertPotatoBoots);
		simpleArmor(DAContent.infusedPotatoHelm);
		simpleArmor(DAContent.infusedPotatoChest);
		simpleArmor(DAContent.infusedPotatoLegs);
		simpleArmor(DAContent.infusedPotatoBoots);
		simpleItem(DAContent.chaosContainer, "item/tools/animated");
		simpleCurios(DAContent.necklaceWyvern);
		simpleCurios(DAContent.necklaceDraconic);
		simpleCurios(DAContent.necklaceChaotic);
		//simpleCurios(DAContent.harnessWyvern);
		//simpleCurios(DAContent.harnessDraconic);
		//simpleCurios(DAContent.harnessChaotic);
		simpleItem(DAContent.hermal, ResourceLocation.fromNamespaceAndPath("minecraft", "item/poisonous_potato"));
		simpleModule(DAModules.chaoticAutoFeed);
		//simpleModule(DAModules.draconicTickAccel);
		//simpleModule(DAModules.chaoticTickAccel);
		simpleModule(DAModules.semiStableChaos);
		simpleModule(DAModules.stableChaos);
		simpleModule(DAModules.unstableChaos);
		simpleModule(DAModules.chaosInjector);
	}

	private void simpleItem(DeferredHolder<? extends Item, ? extends Item> item) {
		simpleItem(item, "item/crafting");
	}

	private void simpleArmor(DeferredHolder<? extends Item, ? extends Item> item) {
		simpleItem(item, "item/armor");
	}

	private void simpleCurios(DeferredHolder<? extends Item, ? extends Item> item) {
		simpleItem(item, "item/curios");
	}

	protected void simpleItem(DeferredHolder<? extends Item, ? extends Item> item, String textureFolder) {
		ResourceLocation reg = item.getId();
		simpleItem(item, ResourceLocation.fromNamespaceAndPath(reg.getNamespace(), textureFolder + "/" + reg.getPath()));
	}

	protected void simpleItem(DeferredHolder<? extends Item, ? extends Item> item, ResourceLocation texture) {
		ResourceLocation reg = item.getId();
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture);
	}

	protected void simpleItem(Item item, ResourceLocation texture) {
		ResourceLocation reg = BuiltInRegistries.ITEM.getKey(item);
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture);
	}

	private void simpleModule(DeferredHolder<? extends Module<?>, ? extends Module<?>> module) {
		simpleModule(module, "item/modules");
	}

	private void simpleModule(DeferredHolder<? extends Module<?>, ? extends Module<?>> module, String textureFolder) {
		if (module == null || !module.isBound()) return;
		Module<?> boundModule = module.get();
		ResourceLocation reg = ModuleRegistry.getRegistry().getKey(boundModule);
		simpleItem(boundModule.getItem(), ResourceLocation.fromNamespaceAndPath(reg.getNamespace(), textureFolder + "/" + reg.getPath().replace("_module", "")));
	}

	protected void multiLayerItem(DeferredHolder<? extends Item, ? extends Item> item, ResourceLocation texture, ResourceLocation overlay) {
		ResourceLocation reg = item.getId();
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture)
		.texture("layer1", overlay);
	}

	protected void multiLayerItem(Item item, ResourceLocation texture, ResourceLocation overlay) {
		ResourceLocation reg = BuiltInRegistries.ITEM.getKey(item);
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture)
		.texture("layer1", overlay);
	}

	protected void blockItem(DeferredHolder<? extends Block, ? extends Block> block) {
		if (block == null) return;
		ResourceLocation reg = block.getId();
		blockItem(block, ResourceLocation.fromNamespaceAndPath(reg.getNamespace(), "block/" + reg.getPath()));
	}

	protected void blockItem(DeferredHolder<? extends Block, ? extends Block> block, ResourceLocation blockModel) {
		if (block == null) return;
		ResourceLocation reg = block.getId();
		getBuilder(reg.getPath()).parent(new ModelFile.UncheckedModelFile(blockModel));
	}

	protected void dummyBlock(DeferredHolder<? extends Block, ? extends Block> block) {
		getBuilder(block.getId().getPath())//
		.parent(new ModelFile.UncheckedModelFile("builtin/generated"));
	}

	protected void dummyItem(DeferredHolder<? extends Item, ? extends Item> item) {
		getBuilder(item.getId().getPath())//
		.parent(new ModelFile.UncheckedModelFile("builtin/generated"));
	}

	@Override
	public String getName() {
		return "Draconic Additions Item Models";
	}
}
