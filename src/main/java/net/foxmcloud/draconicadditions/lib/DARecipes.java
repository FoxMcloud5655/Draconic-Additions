package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.draconicevolution.DEFeatures.*;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addFusion;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addFusionTool;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShaped;
import static com.brandon3055.draconicevolution.lib.RecipeManager.addShapeless;
import static com.brandon3055.draconicevolution.lib.RecipeManager.RecipeDifficulty.*;
import static net.foxmcloud.draconicadditions.DAFeatures.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

import com.brandon3055.draconicevolution.utils.LogHelper;

import net.minecraft.item.ItemStack;

public class DARecipes {
    public static void addRecipes() {
    	
    	//Ingredients
    	
    	addShaped(ALL, inertPotatoHelm, "AAA", "A A", 'A', POISONOUS_POTATO);
    	addShaped(ALL, inertPotatoChest, "A A", "AAA", "AAA", 'A', POISONOUS_POTATO);
    	addShaped(ALL, inertPotatoLegs, "AAA", "A A", "A A", 'A', POISONOUS_POTATO);
    	addShaped(ALL, inertPotatoBoots, "A A", "A A", 'A', POISONOUS_POTATO);
    	addShapeless(ALL, new ItemStack(chaosShard, 9, 1), new ItemStack(chaosShard, 1, 0));
    	addShapeless(ALL, new ItemStack(chaosShard, 9, 2), new ItemStack(chaosShard, 1, 1));
    	addShapeless(ALL, new ItemStack(chaosShard, 9, 3), new ItemStack(chaosShard, 1, 2));
        addFusion(NORMAL, new ItemStack(chaoticEnergyCore), new ItemStack(draconicEnergyCore), 100000000, 3, new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), new ItemStack(chaosShard, 1, 1), "blockRedstone", "blockRedstone", "blockRedstone", "blockRedstone");
        addFusion(HARD, new ItemStack(chaoticEnergyCore), new ItemStack(chaosShard, 1, 0), 200000000, 3, draconicEnergyCore, draconicEnergyCore, draconicEnergyCore, draconicEnergyCore, "blockDraconium", "blockDraconium", "blockDraconium", "blockDraconium");

    	//Potato Armor
    	
    	addShaped(NORMAL, infusedPotatoHelm, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoHelm, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoHelm, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoHelm, 'E', wyvernEnergyCore);
    	addShaped(NORMAL, infusedPotatoChest, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoChest, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoChest, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoChest, 'E', wyvernEnergyCore);
    	addShaped(NORMAL, infusedPotatoLegs, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoLegs, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoLegs, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoLegs, 'E', wyvernEnergyCore);
    	addShaped(NORMAL, infusedPotatoBoots, "ABA", "ACA", "ADA", 'A', "ingotDraconium", 'B', draconicCore, 'C', inertPotatoBoots, 'D', wyvernEnergyCore);
    	addShaped(HARD, infusedPotatoBoots, "ABA", "CDC", "AEA", 'A', "blockDraconium", 'B', draconicCore, 'C', DIAMOND_BLOCK, 'D', inertPotatoBoots, 'E', wyvernEnergyCore);

    	//Chaotic Armor
    	
        addFusionTool(NORMAL, new ItemStack(chaoticHelm), new ItemStack(draconicHelm), 3200000, 3, new ItemStack(chaosShard, 1, 1), chaoticCore, new ItemStack(chaosShard, 1, 1), chaoticEnergyCore);
        addFusionTool(HARD, new ItemStack(chaoticHelm), new ItemStack(draconicHelm), 64000000, 3, new ItemStack(chaosShard, 1, 0), chaoticCore, chaoticEnergyCore);
        addFusionTool(NORMAL, new ItemStack(chaoticChest), new ItemStack(draconicChest), 3200000, 3, new ItemStack(chaosShard, 1, 1), chaoticCore, new ItemStack(chaosShard, 1, 1), chaoticEnergyCore);
        addFusionTool(HARD, new ItemStack(chaoticChest), new ItemStack(draconicChest), 64000000, 3, new ItemStack(chaosShard, 1, 0), chaoticCore, chaoticEnergyCore);
        addFusionTool(NORMAL, new ItemStack(chaoticLegs), new ItemStack(draconicLegs), 3200000, 3, new ItemStack(chaosShard, 1, 1), chaoticCore, new ItemStack(chaosShard, 1, 1), chaoticEnergyCore);
        addFusionTool(HARD, new ItemStack(chaoticLegs), new ItemStack(draconicLegs), 64000000, 3, new ItemStack(chaosShard, 1, 0), chaoticCore, chaoticEnergyCore);
        addFusionTool(NORMAL, new ItemStack(chaoticBoots), new ItemStack(draconicBoots), 3200000, 3, new ItemStack(chaosShard, 1, 1), chaoticCore, new ItemStack(chaosShard, 1, 1), chaoticEnergyCore);
        addFusionTool(HARD, new ItemStack(chaoticBoots), new ItemStack(draconicBoots), 64000000, 3, new ItemStack(chaosShard, 1, 0), chaoticCore, chaoticEnergyCore);

    	//Shield Baubles //TODO: Remove comments once updating DE.
    	
    	//addShaped(NORMAL, basicShieldNecklace, "AAA", "ACA", " B ", 'A', GOLD_INGOT, 'B', draconicCore, 'C', wyvernEnergyCore);
    	//addShaped(HARD, basicShieldNecklace, "AAA", "ACA", " B ", 'A', GOLD_BLOCK, 'B', draconicCore, 'C', wyvernEnergyCore);
    	//addShapeless(ALL, wyvernShieldNecklace, basicShieldNecklace, wyvernCore);
    	//addShapeless(ALL, draconicShieldNecklace, wyvernShieldNecklace, awakenedCore);
        
        //Other Baubles
        
        //addShaped(NORMAL, overloadBelt, "AAA", "ACA", " B ", 'A', "ingotDraconium", 'B', awakenedCore, 'C', LEATHER);
    }
}
