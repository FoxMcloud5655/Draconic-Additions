package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.draconicevolution.DEFeatures.draconicCore;
import static com.brandon3055.draconicevolution.DEFeatures.wyvernEnergyCore;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShaped;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.*;
import static net.foxmcloud.draconicadditions.DAFeatures.*;
import static net.minecraft.init.Blocks.DIAMOND_BLOCK;
import static net.minecraft.init.Items.POISONOUS_POTATO;

import com.brandon3055.draconicevolution.utils.LogHelper;

public class DARecipes {
    public static void addRecipes() {
    	addShaped(ALL, inertPotatoHelm, "AAA", "A A", 'A', POISONOUS_POTATO);
    	addShaped(NORMAL, infusedPotatoHelm, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoHelm, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoHelm, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoHelm, 'E', wyvernEnergyCore);
    	addShaped(ALL, inertPotatoChest, "A A", "AAA", "AAA", 'A', POISONOUS_POTATO);
    	addShaped(NORMAL, infusedPotatoChest, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoChest, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoChest, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoChest, 'E', wyvernEnergyCore);
    	addShaped(ALL, inertPotatoLegs, "AAA", "A A", "A A", 'A', POISONOUS_POTATO);
    	addShaped(NORMAL, infusedPotatoLegs, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoLegs, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoLegs, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoLegs, 'E', wyvernEnergyCore);
    	addShaped(ALL, inertPotatoBoots, "A A", "A A", 'A', POISONOUS_POTATO);
    	addShaped(NORMAL, infusedPotatoBoots, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoBoots, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoBoots, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoBoots, 'E', wyvernEnergyCore);
    }
}
