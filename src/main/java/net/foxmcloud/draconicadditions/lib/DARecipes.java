package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.draconicevolution.DEFeatures.*;
import static com.brandon3055.draconicevolution.lib.RecipeManager.*;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.*;
import static net.foxmcloud.draconicadditions.DAFeatures.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

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
		addFusion(HARD, new ItemStack(chaosStabilizerCore), new ItemStack(reactorCore), 2000000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), infusedObsidian, infusedObsidian);

		// Tools

		addFusionTool(NORMAL, new ItemStack(chaoticStaffOfPower), new ItemStack(draconicStaffOfPower), 16000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), chaoticCore, chaoticEnergyCore);
		addFusionTool(HARD, new ItemStack(chaoticStaffOfPower), new ItemStack(draconicStaffOfPower), 64000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), chaoticCore, chaoticEnergyCore);
		addFusion(ALL, new ItemStack(chaosContainer), new ItemStack(BUCKET), 250000, 2, infusedObsidian, infusedObsidian, infusedObsidian, infusedObsidian);
		addShaped(NORMAL, pwcBasic, "AAB", " AC", " AD", 'A', IRON_INGOT, 'B', new ItemStack(DYE, 1, 0), 'C', REDSTONE, 'D', draconicCore);
		addShaped(HARD, pwcBasic, " C ", "BAB", " D ", 'A', IRON_BLOCK, 'B', new ItemStack(DYE, 1, 0), 'C', REDSTONE, 'D', draconicCore);
		addShapeless(ALL, pwcWyvern, pwcBasic, wyvernCore);
		addShapeless(ALL, pwcDraconic, pwcBasic, awakenedCore);
		addShapeless(ALL, pwcChaotic, pwcBasic, chaoticCore);

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
		addShaped(ALL, itemDrainer, " B ", "CAC", 'A', generator, 'B', IRON_PICKAXE, 'C', awakenedCore);
		addFusion(NORMAL, new ItemStack(chaoticArmorGenerator), new ItemStack(armorGenerator), 50000, 3, chaosContainer);
		addFusion(HARD, new ItemStack(chaoticArmorGenerator), new ItemStack(armorGenerator), 500000, 3, chaosContainer, infusedObsidian);
		addFusion(ALL, new ItemStack(chaosLiquefier), new ItemStack(generator), 2500000, 3, chaosHeart, chaosContainer);
		addShaped(ALL, capacitorSupplier, " B ", "BAB", "BBB", 'A', END_ROD, 'B', IRON_INGOT);
		//addFusion(NORMAL, new ItemStack(chaosInfuser), new ItemStack(chaosLiquefier), 500000, 3, awakenedCore, awakenedCore);
		//addFusion(HARD, new ItemStack(chaosInfuser), new ItemStack(chaosLiquefier), 250000, 3, awakenedCore, draconicEnergyCore, draconicEnergyCore);
		
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
