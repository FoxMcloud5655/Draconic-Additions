package net.foxmcloud.draconicadditions.datagen;

import static com.brandon3055.draconicevolution.init.DETags.Items.*;
import static net.foxmcloud.draconicadditions.lib.DAContent.*;
import static net.foxmcloud.draconicadditions.lib.DAModules.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.datagen.FusionRecipeBuilder;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DEModules;

import codechicken.lib.datagen.recipe.FurnaceRecipeBuilder;
import codechicken.lib.datagen.recipe.RecipeProvider;
import codechicken.lib.datagen.recipe.ShapedRecipeBuilder;
import codechicken.lib.datagen.recipe.ShapelessRecipeBuilder;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public class RecipeGenerator extends RecipeProvider {

	public RecipeGenerator(CompletableFuture<HolderLookup.Provider> registries, PackOutput pOutput) {
		super(registries, pOutput, DraconicAdditions.MODID);
	}

	@Override
	protected void registerRecipes() {
		blocks();
		items();
		modules();
	}

	private void blocks() {
		if (chaosLiquifier != null) {
			fusionRecipe(itemChaosLiquifier, "blocks")
				.catalyst(DEContent.CRAFTING_CORE.get())
				.energy(2500000)
				.techLevel(TechLevel.CHAOTIC)
				.ingredient(chaosContainer);
		}
		
		if (chaosInfuser != null) {
			fusionRecipe(itemChaosInfuser, "blocks")
				.catalyst(DEContent.ENERGY_TRANSFUSER.get())
				.energy(2000000)
				.techLevel(TechLevel.CHAOTIC)
				.ingredient(chaosContainer);
		}
		
		if (chaosExtractor != null) {
			fusionRecipe(itemChaosExtractor, "blocks")
				.catalyst(itemChaosInfuser)
				.energy(100000)
				.techLevel(TechLevel.CHAOTIC)
				.ingredient(DEContent.ENERGY_TRANSFUSER.get());
		}
		
		if (chaosCrystalizer != null) {
			fusionRecipe(itemChaosCrystalizer, "blocks")
				.catalyst(itemChaosLiquifier)
				.energy(100000)
				.techLevel(TechLevel.CHAOTIC)
				.ingredient(DEContent.ENERGY_TRANSFUSER.get());
		}
	}

	private void items() {
		if (inertPotatoHelm != null) {
			shapedRecipe(inertPotatoHelm, "items")
				.patternLine("PBP")
				.patternLine("P P")
				.key('P', Items.POTATO)
				.key('B', Items.POISONOUS_POTATO);
		}

		if (inertPotatoChest != null) {
			shapedRecipe(inertPotatoChest, "items")
				.patternLine("P P")
				.patternLine("PBP")
				.patternLine("PPP")
				.key('P', Items.POTATO)
				.key('B', Items.POISONOUS_POTATO);
		}

		if (inertPotatoLegs != null) {
			shapedRecipe(inertPotatoLegs, "items")
				.patternLine("PBP")
				.patternLine("P P")
				.patternLine("P P")
				.key('P', Items.POTATO)
				.key('B', Items.POISONOUS_POTATO);
		}

		if (inertPotatoBoots != null) {
			shapedRecipe(inertPotatoBoots, "items")
				.patternLine("P P")
				.patternLine("P P")
				.key('P', Items.POTATO);
		}

		if (infusedPotatoHelm != null) {
			shapedRecipe(infusedPotatoHelm, "items")
				.patternLine("DRD")
				.patternLine("RIR")
				.patternLine("DRD")
				.key('D', INGOTS_DRACONIUM)
				.key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('I', inertPotatoHelm);
		}

		if (infusedPotatoChest != null) {
			shapedRecipe(infusedPotatoChest, "items")
				.patternLine("DRD")
				.patternLine("RIR")
				.patternLine("DRD")
				.key('D', INGOTS_DRACONIUM)
				.key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('I', inertPotatoChest);
		}

		if (infusedPotatoLegs != null) {
			shapedRecipe(infusedPotatoLegs, "items")
				.patternLine("DRD")
				.patternLine("RIR")
				.patternLine("DRD")
				.key('D', INGOTS_DRACONIUM)
				.key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('I', inertPotatoLegs);
		}

		if (infusedPotatoBoots != null) {
			shapedRecipe(infusedPotatoBoots, "items")
				.patternLine("DRD")
				.patternLine("RIR")
				.patternLine("DRD")
				.key('D', INGOTS_DRACONIUM)
				.key('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('I', inertPotatoBoots);
		}

		if (chaosContainer != null) {
			fusionRecipe(chaosContainer, "items")
				.catalyst(Items.BUCKET)
				.energy(250000)
				.techLevel(TechLevel.DRACONIC)
				.ingredient(DEContent.INFUSED_OBSIDIAN.get())
				.ingredient(DEContent.INFUSED_OBSIDIAN.get())
				.ingredient(DEContent.ENERGY_CORE_WYVERN.get())
				.ingredient(DEContent.DISLOCATOR.get());
		}

		if (necklaceWyvern != null) {
			shapedRecipe(necklaceWyvern, "items")
				.patternLine("GGG")
				.patternLine("G G")
				.patternLine(" C ")
				.key('G', Tags.Items.INGOTS_GOLD)
				.key('C', DEContent.CORE_WYVERN.get());
		}

		if (necklaceDraconic != null) {
			shapelessRecipe(necklaceDraconic)
				.addIngredient(necklaceWyvern)
				.addIngredient(DEContent.CORE_AWAKENED.get());
		}

		if (necklaceChaotic != null) {
			shapelessRecipe(necklaceChaotic)
				.addIngredient(necklaceDraconic)
				.addIngredient(DEContent.CORE_CHAOTIC.get());
		}

		/*
		if (harnessWyvern != null) {
			shapedRecipe(harnessWyvern, "items")
				.patternLine("DDD")	
				.patternLine("DLD")
				.patternLine("DSD")
				.key('D', INGOTS_DRACONIUM)
				.key('L', DEContent.DISLOCATOR.get())
				.key('S', DEModules.WYVERN_SHIELD_CONTROL.get().getItem());
		}

		if (harnessDraconic != null) {
			shapedRecipe(harnessDraconic, "items")
				.patternLine("AAA")
				.patternLine("AHA")
				.patternLine("ASA")
				.key('A', INGOTS_DRACONIUM_AWAKENED)
				.key('H', harnessWyvern)
				.key('S', DEModules.DRACONIC_SHIELD_CONTROL.get().getItem());
		}

		if (harnessChaotic != null) {
			shapedRecipe(harnessChaotic, "items")
				.patternLine("CCC")
				.patternLine("CHC")
				.patternLine("CSC")
				.key('C', DEContent.CHAOS_FRAG_MEDIUM.get())
				.key('H', harnessDraconic)
				.key('S', DEModules.CHAOTIC_SHIELD_CONTROL.get().getItem());
		}
		*/
		
		if (hermal != null) {
			shapedRecipe(hermal, "items")
				.patternLine("CCC")
				.patternLine("CPC")
				.patternLine("CCC")
				.key('C', DEContent.CHAOS_FRAG_MEDIUM.get())
				.key('P', Items.POISONOUS_POTATO);
		}
	}
	
	private void modules() {
		if (chaoticAutoFeed != null) {
			shapedRecipe(chaoticAutoFeed.get().getItem(), "modules")
				.patternLine("FCF")
				.patternLine("DAD")
				.patternLine("FCF")
				.key('F', DEContent.CHAOS_FRAG_MEDIUM.get())
				.key('D', DEContent.CORE_DRACONIUM.get())
				.key('A', DEModules.DRACONIC_AUTO_FEED.get().getItem())
				.key('C', Items.COOKIE);
		}

		/*
		if (draconicTickAccel != null) {
			shapedRecipe(draconicTickAccel.get().getItem(), "modules")
				.patternLine("A A")
				.patternLine("SCS")
				.patternLine("A A")
				.key('A', INGOTS_DRACONIUM_AWAKENED)
				.key('S', DEModules.WYVERN_SPEED.get().getItem())
				.key('C', DEContent.MODULE_CORE.get());
		}

		if (chaoticTickAccel != null) {
			shapedRecipe(chaoticTickAccel.get().getItem(), "modules")
				.patternLine("C C")
				.patternLine("SDS")
				.patternLine("C C")
				.key('C', DEContent.CHAOS_FRAG_MEDIUM)
				.key('S', DEModules.DRACONIC_SPEED.get().getItem())
				.key('D', draconicTickAccel.get().getItem());
		}
		*/
		
		if (semiStableChaos != null) {
			shapedRecipe(semiStableChaos.get().getItem(), "modules")
				.patternLine("CRC")
				.patternLine("SHS")
				.patternLine("CRC")
				.key('C', DEContent.CHAOS_FRAG_LARGE)
				.key('S', DEModules.DRACONIC_SHIELD_CAPACITY.get().getItem())
				.key('R', DEModules.DRACONIC_SHIELD_RECOVERY.get().getItem())
				.key('H', DEContent.DRAGON_HEART);
		}
		
		if (stableChaos != null) {
			shapedRecipe(stableChaos.get().getItem(), "modules")
				.patternLine("CRC")
				.patternLine("RHR")
				.patternLine("CRC")
				.key('C', DEContent.CHAOS_FRAG_LARGE)
				.key('R', DEModules.DRACONIC_SHIELD_RECOVERY.get().getItem())
				.key('H', DEContent.DRAGON_HEART);
		}
		
		if (unstableChaos != null) {
			shapedRecipe(unstableChaos.get().getItem(), "modules")
				.patternLine("CSC")
				.patternLine("SHS")
				.patternLine("CSC")
				.key('C', DEContent.CHAOS_FRAG_LARGE)
				.key('S', DEModules.DRACONIC_SHIELD_CAPACITY.get().getItem())
				.key('H', DEContent.DRAGON_HEART);
		}
		
		if (chaosInjector != null) {
			shapedRecipe(chaosInjector.get().getItem(), "modules")
				.patternLine("CGC")
				.patternLine("GMG")
				.patternLine("CGC")
				.key('C', DEContent.CHAOS_FRAG_MEDIUM)
				.key('G', Items.GLASS)
				.key('M', DEContent.MODULE_CORE);
		}
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result, ResourceLocation id) {
		return builder(FusionRecipeBuilder.builder(result.get(), 1, id));
	}

	protected FusionRecipeBuilder fusionRecipe(ItemLike result) {
		return builder(FusionRecipeBuilder.builder(result));
	}

	protected FusionRecipeBuilder fusionRecipe(ItemLike result, int count) {
		return builder(FusionRecipeBuilder.builder(new ItemStack(result, count)));
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result) {
		return builder(FusionRecipeBuilder.builder(result.get(), 1));
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(FusionRecipeBuilder.builder(result.get(), 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result, String folder, Function<String, String> customPath) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(FusionRecipeBuilder.builder(result.get(), 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + customPath.apply(id.getPath()))));
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result, int count, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(FusionRecipeBuilder.builder(result.get(), count, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected FusionRecipeBuilder fusionRecipe(Supplier<? extends ItemLike> result, int count) {
		return builder(FusionRecipeBuilder.builder(new ItemStack(result.get(), count)));
	}

	protected FusionRecipeBuilder fusionRecipe(ItemStack result) {
		return builder(FusionRecipeBuilder.builder(result, BuiltInRegistries.ITEM.getKey(result.getItem())));
	}

	protected FusionRecipeBuilder fusionRecipe(ItemStack result, ResourceLocation id) {
		return builder(FusionRecipeBuilder.builder(result, id));
	}

	protected FurnaceRecipeBuilder smelting(Supplier<? extends ItemLike> result, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(FurnaceRecipeBuilder.smelting(result.get(), 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected FurnaceRecipeBuilder smelting(Supplier<? extends ItemLike> result, String folder, Function<String, String> customPath) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(FurnaceRecipeBuilder.smelting(result.get(), 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + customPath.apply(id.getPath()))));
	}

	protected ShapedRecipeBuilder shapedRecipe(Supplier<? extends ItemLike> result, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapedRecipeBuilder.builder(result.get(), 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected ShapedRecipeBuilder shapedRecipe(ItemLike result, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.asItem());
		return builder(ShapedRecipeBuilder.builder(result, 1, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected ShapedRecipeBuilder shapedRecipe(Supplier<? extends ItemLike> result, int count, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapedRecipeBuilder.builder(result.get(), count, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected ShapedRecipeBuilder shapedRecipe(Supplier<? extends ItemLike> result, int count, String folder, Function<String, String> customPath) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapedRecipeBuilder.builder(result.get(), count, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + customPath.apply(id.getPath()))));
	}

	protected ShapelessRecipeBuilder shapelessRecipe(Supplier<? extends ItemLike> result, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapelessRecipeBuilder.builder(new ItemStack(result.get(), 1), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected ShapelessRecipeBuilder shapelessRecipe(Supplier<? extends ItemLike> result, String folder, Function<String, String> customPath) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapelessRecipeBuilder.builder(new ItemStack(result.get(), 1), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + customPath.apply(id.getPath()))));
	}

	protected ShapelessRecipeBuilder shapelessRecipe(Supplier<? extends ItemLike> result, int count, String folder) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.get().asItem());
		return builder(ShapelessRecipeBuilder.builder(new ItemStack(result.get(), count), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + id.getPath())));
	}

	protected ShapelessRecipeBuilder shapelessRecipe(ItemLike result, int count, ResourceLocation id) {
		return builder(ShapelessRecipeBuilder.builder(new ItemStack(result, count), id));
	}

	protected ShapelessRecipeBuilder shapelessRecipe(ItemLike result, int count, String folder, Function<String, String> customPath) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.asItem());
		return builder(ShapelessRecipeBuilder.builder(new ItemStack(result, count), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), folder + "/" + customPath.apply(id.getPath()))));
	}
}
