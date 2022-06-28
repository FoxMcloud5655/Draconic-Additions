package net.foxmcloud.draconicadditions.datagen;

import static com.brandon3055.draconicevolution.init.DEContent.*;
import static com.brandon3055.draconicevolution.init.DEModules.*;
import static com.brandon3055.draconicevolution.init.DETags.Items.*;
import static net.foxmcloud.draconicadditions.items.DAContent.*;
import static net.foxmcloud.draconicadditions.items.DAModules.*;
import static net.minecraftforge.common.Tags.Items.*;
import static net.minecraft.item.Items.*;

import java.io.IOException;
import java.util.function.Consumer;

import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeGenerator extends RecipeProvider {

	public RecipeGenerator(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		items(consumer);
	}

	private static void items(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(inertPotatoHelm)
		.pattern("PBP")
		.pattern("P P")
		.define('P', POTATO)
		.define('B', POISONOUS_POTATO)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", inertPotatoHelm));

		ShapedRecipeBuilder.shaped(inertPotatoChest)
		.pattern("P P")
		.pattern("PBP")
		.pattern("PPP")
		.define('P', POTATO)
		.define('B', POISONOUS_POTATO)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", inertPotatoChest));

		ShapedRecipeBuilder.shaped(inertPotatoLegs)
		.pattern("PBP")
		.pattern("P P")
		.pattern("P P")
		.define('P', POTATO)
		.define('B', POISONOUS_POTATO)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", inertPotatoLegs));

		ShapedRecipeBuilder.shaped(inertPotatoBoots)
		.pattern("P P")
		.pattern("P P")
		.define('P', POTATO)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", inertPotatoBoots));
		
		ShapedRecipeBuilder.shaped(infusedPotatoHelm)
		.pattern("DRD")
		.pattern("RIR")
		.pattern("DRD")
		.define('D', INGOTS_DRACONIUM)
		.define('R', REDSTONE_BLOCK)
		.define('I', inertPotatoHelm)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", infusedPotatoHelm));
		
		ShapedRecipeBuilder.shaped(infusedPotatoChest)
		.pattern("DRD")
		.pattern("RIR")
		.pattern("DRD")
		.define('D', INGOTS_DRACONIUM)
		.define('R', REDSTONE_BLOCK)
		.define('I', inertPotatoChest)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", infusedPotatoChest));
		
		ShapedRecipeBuilder.shaped(infusedPotatoLegs)
		.pattern("DRD")
		.pattern("RIR")
		.pattern("DRD")
		.define('D', INGOTS_DRACONIUM)
		.define('R', REDSTONE_BLOCK)
		.define('I', inertPotatoLegs)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", infusedPotatoLegs));
		
		ShapedRecipeBuilder.shaped(infusedPotatoBoots)
		.pattern("DRD")
		.pattern("RIR")
		.pattern("DRD")
		.define('D', INGOTS_DRACONIUM)
		.define('R', REDSTONE_BLOCK)
		.define('I', inertPotatoBoots)
		.unlockedBy("has_draconium", has(DEContent.ingot_draconium))
		.save(consumer, folder("items", infusedPotatoBoots));

		ShapedRecipeBuilder.shaped(necklaceWyvern)
		.pattern("GGG")
		.pattern("G G")
		.pattern(" C ")
		.define('G', INGOTS_GOLD)
		.define('C', core_wyvern)
		.unlockedBy("has_core_wyvern", has(core_wyvern))
		.save(consumer, folder("items", necklaceWyvern));

		ShapelessRecipeBuilder.shapeless(necklaceDraconic)
		.requires(necklaceWyvern)
		.requires(core_awakened)
		.unlockedBy("has_core_awakened", has(core_awakened))
		.save(consumer, folder("items", necklaceDraconic));

		ShapelessRecipeBuilder.shapeless(necklaceChaotic)
		.requires(necklaceDraconic)
		.requires(core_chaotic)
		.unlockedBy("has_core_chaotic", has(core_chaotic))
		.save(consumer, folder("items", necklaceChaotic));
		
		ShapedRecipeBuilder.shaped(harnessWyvern)
		.pattern("DDD")
		.pattern("DLD")
		.pattern("DSD")
		.define('D', INGOTS_DRACONIUM)
		.define('L', dislocator)
		.define('S', wyvernShieldControl.getItem())
		.unlockedBy("has_core_wyvern", has(core_wyvern))
		.save(consumer, folder("items", harnessWyvern));
		
		ShapedRecipeBuilder.shaped(harnessDraconic)
		.pattern("AAA")
		.pattern("AHA")
		.pattern("ASA")
		.define('A', INGOTS_DRACONIUM_AWAKENED)
		.define('H', harnessWyvern)
		.define('S', draconicShieldControl.getItem())
		.unlockedBy("has_core_awakened", has(core_awakened))
		.save(consumer, folder("items", harnessDraconic));
		
		ShapedRecipeBuilder.shaped(harnessChaotic)
		.pattern("CCC")
		.pattern("CHC")
		.pattern("CSC")
		.define('C', chaos_frag_medium)
		.define('H', harnessDraconic)
		.define('S', chaoticShieldControl.getItem())
		.unlockedBy("has_core_chaotic", has(core_chaotic))
		.save(consumer, folder("items", harnessChaotic));

		ShapedRecipeBuilder.shaped(chaoticAutoFeed.getItem())
		.pattern("FCF")
		.pattern("DAD")
		.pattern("FCF")
		.define('F', chaos_frag_medium)
		.define('D', core_draconium)
		.define('A', draconicAutoFeed.getItem())
		.define('C', COOKIE)
		.unlockedBy("has_module_core", has(module_core))
		.save(consumer, folder("modules", chaoticAutoFeed));
		
		ShapedRecipeBuilder.shaped(draconicTickAccel.getItem())
		.pattern("A A")
		.pattern("SCS")
		.pattern("A A")
		.define('A', INGOTS_DRACONIUM_AWAKENED)
		.define('S', wyvernSpeed.getItem())
		.define('C', module_core)
		.unlockedBy("has_module_core", has(module_core))
		.save(consumer, folder("modules", draconicTickAccel));
		
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

	public static String folder(String folder, IForgeRegistryEntry<?> key) {
		return DraconicAdditions.MODID + ":" + folder + "/" + key.getRegistryName().getPath();
	}

	public static String folder(String folder, String name) {
		return DraconicAdditions.MODID + ":" + folder + "/" + name;
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		super.run(cache);
	}

	public static class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
		public NBTIngredient(ItemStack stack) {
			super(stack);
		}
	}
}