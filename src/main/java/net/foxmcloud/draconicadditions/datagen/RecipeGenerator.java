package net.foxmcloud.draconicadditions.datagen;

import static com.brandon3055.draconicevolution.init.DEContent.*;
import static com.brandon3055.draconicevolution.init.DEModules.*;
import static com.brandon3055.draconicevolution.init.DETags.Items.*;
import static net.foxmcloud.draconicadditions.lib.DAContent.*;
import static net.foxmcloud.draconicadditions.lib.DAModules.*;

import java.util.function.Consumer;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.datagen.FusionRecipeBuilder;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeGenerator extends RecipeProvider {

	public RecipeGenerator(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		blocks(consumer);
		items(consumer);
		modules(consumer);
	}

	private static void blocks(Consumer<FinishedRecipe> consumer) {
		if (chaosLiquefier != null) {
			FusionRecipeBuilder.fusionRecipe(chaosLiquefier)
			.catalyst(DEContent.crafting_core)
			.energy(2500000)
			.techLevel(TechLevel.CHAOTIC)
			.ingredient(chaosHeart)
			.ingredient(chaosContainer)
			.build(consumer, folder("blocks", chaosLiquefier));
		}
		
		if (chaosInfuser != null) {
			FusionRecipeBuilder.fusionRecipe(chaosInfuser)
			.catalyst(DEContent.energy_transfuser)
			.energy(2000000)
			.techLevel(TechLevel.CHAOTIC)
			.ingredient(chaosContainer)
			.build(consumer, folder("blocks", chaosInfuser));
		}
	}

	private static void items(Consumer<FinishedRecipe> consumer) {
		if (inertPotatoHelm != null) {
			ShapedRecipeBuilder.shaped(inertPotatoHelm)
			.pattern("PBP")
			.pattern("P P")
			.define('P', Items.POTATO)
			.define('B', Items.POISONOUS_POTATO)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", inertPotatoHelm));
		}

		if (inertPotatoChest != null) {
			ShapedRecipeBuilder.shaped(inertPotatoChest)
			.pattern("P P")
			.pattern("PBP")
			.pattern("PPP")
			.define('P', Items.POTATO)
			.define('B', Items.POISONOUS_POTATO)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", inertPotatoChest));
		}

		if (inertPotatoLegs != null) {
			ShapedRecipeBuilder.shaped(inertPotatoLegs)
			.pattern("PBP")
			.pattern("P P")
			.pattern("P P")
			.define('P', Items.POTATO)
			.define('B', Items.POISONOUS_POTATO)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", inertPotatoLegs));
		}

		if (inertPotatoBoots != null) {
			ShapedRecipeBuilder.shaped(inertPotatoBoots)
			.pattern("P P")
			.pattern("P P")
			.define('P', Items.POTATO)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", inertPotatoBoots));
		}

		if (infusedPotatoHelm != null) {
			ShapedRecipeBuilder.shaped(infusedPotatoHelm)
			.pattern("DRD")
			.pattern("RIR")
			.pattern("DRD")
			.define('D', INGOTS_DRACONIUM)
			.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			.define('I', inertPotatoHelm)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", infusedPotatoHelm));
		}

		if (infusedPotatoChest != null) {
			ShapedRecipeBuilder.shaped(infusedPotatoChest)
			.pattern("DRD")
			.pattern("RIR")
			.pattern("DRD")
			.define('D', INGOTS_DRACONIUM)
			.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			.define('I', inertPotatoChest)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", infusedPotatoChest));
		}

		if (infusedPotatoLegs != null) {
			ShapedRecipeBuilder.shaped(infusedPotatoLegs)
			.pattern("DRD")
			.pattern("RIR")
			.pattern("DRD")
			.define('D', INGOTS_DRACONIUM)
			.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			.define('I', inertPotatoLegs)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", infusedPotatoLegs));
		}

		if (infusedPotatoBoots != null) {
			ShapedRecipeBuilder.shaped(infusedPotatoBoots)
			.pattern("DRD")
			.pattern("RIR")
			.pattern("DRD")
			.define('D', INGOTS_DRACONIUM)
			.define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
			.define('I', inertPotatoBoots)
			.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
			.save(consumer, folder("items", infusedPotatoBoots));
		}

		if (chaosContainer != null) {
			FusionRecipeBuilder.fusionRecipe(chaosContainer)
			.catalyst(Items.BUCKET)
			.energy(250000)
			.techLevel(TechLevel.DRACONIC)
			.ingredient(infused_obsidian)
			.ingredient(infused_obsidian)
			.ingredient(energy_core_wyvern)
			.ingredient(dislocator)
			.build(consumer, folder("items", chaosContainer));
		}

		if (necklaceWyvern != null) {
			ShapedRecipeBuilder.shaped(necklaceWyvern)
			.pattern("GGG")
			.pattern("G G")
			.pattern(" C ")
			.define('G', Tags.Items.INGOTS_GOLD)
			.define('C', core_wyvern)
			.unlockedBy("has_core_wyvern", has(core_wyvern))
			.save(consumer, folder("items", necklaceWyvern));
		}

		if (necklaceDraconic != null) {
			ShapelessRecipeBuilder.shapeless(necklaceDraconic)
			.requires(necklaceWyvern)
			.requires(core_awakened)
			.unlockedBy("has_core_awakened", has(core_awakened))
			.save(consumer, folder("items", necklaceDraconic));
		}

		if (necklaceChaotic != null) {
			ShapelessRecipeBuilder.shapeless(necklaceChaotic)
			.requires(necklaceDraconic)
			.requires(core_chaotic)
			.unlockedBy("has_core_chaotic", has(core_chaotic))
			.save(consumer, folder("items", necklaceChaotic));
		}

		if (harnessWyvern != null) {
			ShapedRecipeBuilder.shaped(harnessWyvern)
			.pattern("DDD")	
			.pattern("DLD")
			.pattern("DSD")
			.define('D', INGOTS_DRACONIUM)
			.define('L', dislocator)
			.define('S', wyvernShieldControl.getItem())
			.unlockedBy("has_core_wyvern", has(core_wyvern))
			.save(consumer, folder("items", harnessWyvern));
		}

		if (harnessDraconic != null) {
			ShapedRecipeBuilder.shaped(harnessDraconic)
			.pattern("AAA")
			.pattern("AHA")
			.pattern("ASA")
			.define('A', INGOTS_DRACONIUM_AWAKENED)
			.define('H', harnessWyvern)
			.define('S', draconicShieldControl.getItem())
			.unlockedBy("has_core_awakened", has(core_awakened))
			.save(consumer, folder("items", harnessDraconic));
		}

		if (harnessChaotic != null) {
			ShapedRecipeBuilder.shaped(harnessChaotic)
			.pattern("CCC")
			.pattern("CHC")
			.pattern("CSC")
			.define('C', chaos_frag_medium)
			.define('H', harnessDraconic)
			.define('S', chaoticShieldControl.getItem())
			.unlockedBy("has_core_chaotic", has(core_chaotic))
			.save(consumer, folder("items", harnessChaotic));
		}
	}
	
	private static void modules(Consumer<FinishedRecipe> consumer) {
		if (chaoticAutoFeed != null) {
			ShapedRecipeBuilder.shaped(chaoticAutoFeed.getItem())
			.pattern("FCF")
			.pattern("DAD")
			.pattern("FCF")
			.define('F', chaos_frag_medium)
			.define('D', core_draconium)
			.define('A', draconicAutoFeed.getItem())
			.define('C', Items.COOKIE)
			.unlockedBy("has_module_core", has(module_core))
			.save(consumer, folder("modules", chaoticAutoFeed));
		}

		if (draconicTickAccel != null) {
			ShapedRecipeBuilder.shaped(draconicTickAccel.getItem())
			.pattern("A A")
			.pattern("SCS")
			.pattern("A A")
			.define('A', INGOTS_DRACONIUM_AWAKENED)
			.define('S', wyvernSpeed.getItem())
			.define('C', module_core)
			.unlockedBy("has_module_core", has(module_core))
			.save(consumer, folder("modules", draconicTickAccel));
		}

		if (chaoticTickAccel != null) {
			ShapedRecipeBuilder.shaped(chaoticTickAccel.getItem())
			.pattern("C C")
			.pattern("SDS")
			.pattern("C C")
			.define('C', chaos_frag_medium)
			.define('S', draconicSpeed.getItem())
			.define('D', draconicTickAccel.getItem())
			.unlockedBy("has_module_core", has(module_core))
			.save(consumer, folder("modules", chaoticTickAccel));
		}
	}

	public static String folder(String folder, IForgeRegistryEntry<?> key) {
		return DraconicAdditions.MODID + ":" + folder + "/" + key.getRegistryName().getPath();
	}

	public static String folder(String folder, String name) {
		return DraconicAdditions.MODID + ":" + folder + "/" + name;
	}

	@Override
	public void run(HashCache cache) {
		super.run(cache);
	}

	public static class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
		public NBTIngredient(ItemStack stack) {
			super(stack);
		}
	}
}
