package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.draconicevolution.DEFeatures.awakenedCore;
import static com.brandon3055.draconicevolution.DEFeatures.chaosShard;
import static com.brandon3055.draconicevolution.DEFeatures.chaoticCore;
import static com.brandon3055.draconicevolution.DEFeatures.draconicBoots;
import static com.brandon3055.draconicevolution.DEFeatures.draconicChest;
import static com.brandon3055.draconicevolution.DEFeatures.draconicCore;
import static com.brandon3055.draconicevolution.DEFeatures.draconicEnergyCore;
import static com.brandon3055.draconicevolution.DEFeatures.draconicHelm;
import static com.brandon3055.draconicevolution.DEFeatures.draconicLegs;
import static com.brandon3055.draconicevolution.DEFeatures.draconicStaffOfPower;
import static com.brandon3055.draconicevolution.DEFeatures.generator;
import static com.brandon3055.draconicevolution.DEFeatures.infusedObsidian;
import static com.brandon3055.draconicevolution.DEFeatures.reactorCore;
import static com.brandon3055.draconicevolution.DEFeatures.wyvernCore;
import static com.brandon3055.draconicevolution.DEFeatures.wyvernEnergyCore;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addFusion;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addFusionTool;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShaped;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShapeless;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.ALL;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.HARD;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.NORMAL;
import static net.foxmcloud.draconicadditions.DAFeatures.armorGenerator;
import static net.foxmcloud.draconicadditions.DAFeatures.basicShieldNecklace;
import static net.foxmcloud.draconicadditions.DAFeatures.chaosContainer;
import static net.foxmcloud.draconicadditions.DAFeatures.chaosLiquefier;
import static net.foxmcloud.draconicadditions.DAFeatures.chaosStabilizerCore;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticArmorGenerator;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticBoots;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticChest;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticEnergyCore;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticHelm;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticLegs;
import static net.foxmcloud.draconicadditions.DAFeatures.chaoticStaffOfPower;
import static net.foxmcloud.draconicadditions.DAFeatures.draconicShieldNecklace;
import static net.foxmcloud.draconicadditions.DAFeatures.inertPotatoBoots;
import static net.foxmcloud.draconicadditions.DAFeatures.inertPotatoChest;
import static net.foxmcloud.draconicadditions.DAFeatures.inertPotatoHelm;
import static net.foxmcloud.draconicadditions.DAFeatures.inertPotatoLegs;
import static net.foxmcloud.draconicadditions.DAFeatures.inertiaCancelRing;
import static net.foxmcloud.draconicadditions.DAFeatures.infusedPotatoBoots;
import static net.foxmcloud.draconicadditions.DAFeatures.infusedPotatoChest;
import static net.foxmcloud.draconicadditions.DAFeatures.infusedPotatoHelm;
import static net.foxmcloud.draconicadditions.DAFeatures.infusedPotatoLegs;
import static net.foxmcloud.draconicadditions.DAFeatures.overloadBelt;
import static net.foxmcloud.draconicadditions.DAFeatures.vampiricShirt;
import static net.foxmcloud.draconicadditions.DAFeatures.wyvernShieldNecklace;
import static net.minecraft.init.Blocks.DIAMOND_BLOCK;
import static net.minecraft.init.Blocks.GOLD_BLOCK;
import static net.minecraft.init.Blocks.WOOL;
import static net.minecraft.init.Items.BUCKET;
import static net.minecraft.init.Items.GOLD_INGOT;
import static net.minecraft.init.Items.IRON_CHESTPLATE;
import static net.minecraft.init.Items.LEATHER;
import static net.minecraft.init.Items.POISONOUS_POTATO;
import static net.minecraft.init.Items.POTATO;

import net.minecraft.item.ItemStack;

public class DARecipes {
	public static void addRecipes() {

		// Ingredients

		addShaped(ALL, inertPotatoHelm, "BAB", "B B", 'A', POISONOUS_POTATO, 'B', POTATO);
		addShaped(ALL, inertPotatoChest, "B B", "BAB", "BAB", 'A', POISONOUS_POTATO, 'B', POTATO);
		addShaped(ALL, inertPotatoLegs, "ABA", "B B", "B B", 'A', POISONOUS_POTATO, 'B', POTATO);
		addShaped(ALL, inertPotatoBoots, "A A", "A A", 'A', POTATO);
		addShapeless(ALL, new ItemStack(chaosShard, 9, 1), new ItemStack(chaosShard, 1, 0));
		addShapeless(ALL, new ItemStack(chaosShard, 9, 2), new ItemStack(chaosShard, 1, 1));
		addShapeless(ALL, new ItemStack(chaosShard, 9, 3), new ItemStack(chaosShard, 1, 2));
		addFusion(NORMAL, new ItemStack(chaosStabilizerCore), new ItemStack(reactorCore), 1250000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), infusedObsidian, infusedObsidian);
		addFusion(HARD, new ItemStack(chaosStabilizerCore), new ItemStack(reactorCore), 2000000000, 3,  new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), infusedObsidian, infusedObsidian);

		// Tools
		
		addFusionTool(NORMAL, new ItemStack(chaoticStaffOfPower), new ItemStack(draconicStaffOfPower), 16000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), chaoticCore, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticStaffOfPower), new ItemStack(draconicStaffOfPower), 64000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), chaoticCore, chaoticEnergyCore);
		addFusion(ALL, new ItemStack(chaosContainer), new ItemStack(BUCKET), 250000, 2, infusedObsidian, infusedObsidian, infusedObsidian, infusedObsidian);
		
		// Potato Armor

		addShaped(NORMAL, infusedPotatoHelm, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoHelm, 'D', wyvernEnergyCore);
		addShaped(HARD, infusedPotatoHelm, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoHelm, 'E', wyvernEnergyCore);
		addShaped(NORMAL, infusedPotatoChest, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoChest, 'D', wyvernEnergyCore);
		addShaped(HARD, infusedPotatoChest, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoChest, 'E', wyvernEnergyCore);
		addShaped(NORMAL, infusedPotatoLegs, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoLegs, 'D', wyvernEnergyCore);
		addShaped(HARD, infusedPotatoLegs, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoLegs, 'E', wyvernEnergyCore);
		addShaped(NORMAL, infusedPotatoBoots, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoBoots, 'D', wyvernEnergyCore);
		addShaped(HARD, infusedPotatoBoots, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoBoots, 'E', wyvernEnergyCore);

		// Chaotic Armor

		addFusionTool(NORMAL, new ItemStack(chaoticHelm), new ItemStack(draconicHelm), 3200000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticHelm), new ItemStack(draconicHelm), 64000000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(NORMAL, new ItemStack(chaoticChest), new ItemStack(draconicChest), 3200000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticChest), new ItemStack(draconicChest), 64000000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(NORMAL, new ItemStack(chaoticLegs), new ItemStack(draconicLegs), 3200000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticLegs), new ItemStack(draconicLegs), 64000000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(NORMAL, new ItemStack(chaoticBoots), new ItemStack(draconicBoots), 3200000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticBoots), new ItemStack(draconicBoots), 64000000, 3, infusedObsidian, chaoticCore, infusedObsidian, chaoticEnergyCore);

		// Blocks
		
		addFusion(NORMAL, new ItemStack(chaoticEnergyCore), new ItemStack(draconicEnergyCore), 1000000000, 2, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), "blockRedstone", "blockRedstone", "blockRedstone", "blockRedstone");
		addFusion(HARD, new ItemStack(chaoticEnergyCore), new ItemStack(chaosShard, 1, 0), 2000000000, 2, draconicEnergyCore, draconicEnergyCore, draconicEnergyCore, draconicEnergyCore, "blockDraconium", "blockDraconium", "blockDraconium", "blockDraconium");
		addShapeless(ALL, armorGenerator, generator, IRON_CHESTPLATE);
		addFusion(NORMAL, new ItemStack(chaoticArmorGenerator), new ItemStack(armorGenerator), 50000, 3, chaosContainer);
		addFusion(HARD, new ItemStack(chaoticArmorGenerator), new ItemStack(armorGenerator), 500000, 3, chaosContainer, infusedObsidian);
		addFusion(ALL, new ItemStack(chaosLiquefier), new ItemStack(generator), 2500000, 3, reactorCore, chaosContainer);
		
		// Shield Baubles

		addShaped(NORMAL, basicShieldNecklace, "AAA", "ACA", " B ", 'A', GOLD_INGOT, 'B', draconicCore, 'C', wyvernEnergyCore);
		addShaped(HARD, basicShieldNecklace, " A ", "ACA", " B ", 'A', GOLD_BLOCK, 'B', draconicCore, 'C', wyvernEnergyCore);
		addShapeless(ALL, wyvernShieldNecklace, basicShieldNecklace, wyvernCore);
		addShapeless(ALL, draconicShieldNecklace, wyvernShieldNecklace, awakenedCore);

		// Other Baubles

		addShaped(ALL, overloadBelt, "AAA", "ACA", " B ", 'A', "ingotDraconium", 'B', awakenedCore, 'C', LEATHER);
		addShaped(ALL, vampiricShirt, "A A", "BCB", "ACA", 'A', "ingotDraconiumAwakened", 'B', awakenedCore, 'C', WOOL);
		addShaped(NORMAL, inertiaCancelRing, "ABA", "B B", "ABA", 'A', GOLD_INGOT, 'B', wyvernEnergyCore);
		addShaped(HARD, inertiaCancelRing, "ABA", "B B", "ABA", 'A', GOLD_INGOT, 'B', wyvernCore);
	}
}
