package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.draconicevolution.DEFeatures.draconicCore;
import static com.brandon3055.draconicevolution.DEFeatures.wyvernEnergyCore;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShaped;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.HARD;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.NORMAL;
import static net.foxmcloud.draconicadditions.DAFeatures.potatoLegs;
import static net.minecraft.init.Blocks.DIAMOND_BLOCK;
import static net.minecraft.init.Items.POISONOUS_POTATO;

public class DARecipies {
    public static void addRecipes() {
    	//addShaped(NORMAL, wyvernHelm, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', wyvernCore, 'C', DIAMOND_HELMET, 'D', wyvernEnergyCore);
    	//addShaped(HARD, wyvernHelm, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', wyvernCore, 'C', "netherStar", 'D', DIAMOND_HELMET, 'E', wyvernEnergyCore);
    	//addShaped(NORMAL, wyvernChest, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', wyvernCore, 'C', DIAMOND_CHESTPLATE, 'D', wyvernEnergyCore);
    	//addShaped(HARD, wyvernChest, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', wyvernCore, 'C', "netherStar", 'D', DIAMOND_CHESTPLATE, 'E', wyvernEnergyCore);
    	addShaped(NORMAL, potatoLegs, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', POISONOUS_POTATO, 'D', wyvernEnergyCore);
    	addShaped(HARD, potatoLegs, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', POISONOUS_POTATO, 'E', wyvernEnergyCore);
    	//addShaped(NORMAL, wyvernBoots, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', wyvernCore, 'C', DIAMOND_BOOTS, 'D', wyvernEnergyCore);
    	//addShaped(HARD, wyvernBoots, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', wyvernCore, 'C', "netherStar", 'D', DIAMOND_BOOTS, 'E', wyvernEnergyCore);
    }
}
