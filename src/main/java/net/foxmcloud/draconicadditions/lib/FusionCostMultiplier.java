package net.foxmcloud.draconicadditions.lib;

import java.util.List;

import org.apache.logging.log4j.Level;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class FusionCostMultiplier {
	@ModConfigProperty(category = "Misc Tweaks", name = "Fusion Crafting Cost Multiplier", comment = "Allows you to adjust the multiplier on fusion crafting.  Mainly intended for modpack authors; this config is NOT automatically updated when you join another server, and you must restart Minecraft for this config to take effect.", requiresMCRestart = true, requiresSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "10")
	public static double POWER_COST_MULTIPLIER = 1.0D;
	
	public static void postInit() {
		if (POWER_COST_MULTIPLIER == 1.0D) {
			DraconicAdditions.logger.log(Level.INFO, "Power costs are not modified. Skipping recipe rebalance.");
			return;
		}
		List<IFusionRecipe> recipes = FusionRecipeAPI.getRecipes();
		DraconicAdditions.logger.log(Level.INFO, "Starting takeover of Draconic Evolution. Don't worry, I won't be taking you over for very long!");
		LogHelper.info("wat, i didnt sign up for thi- !!!");
		LogHelper.info("Takeover complete. Beginning recipe rebalancing at power level " + POWER_COST_MULTIPLIER + ".");
		for (IFusionRecipe oldRecipe : recipes) {
			if (!oldRecipe.getRecipeCatalyst().isEmpty()) {
				LogHelper.dev(oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " was " + oldRecipe.getIngredientEnergyCost());
				FusionRecipeAPI.removeRecipe(oldRecipe);
				IFusionRecipe newRecipe = new SimpleFusionRecipe(
						oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()),
						oldRecipe.getRecipeCatalyst(),
						(long)(oldRecipe.getIngredientEnergyCost() * POWER_COST_MULTIPLIER),
						oldRecipe.getRecipeTier(),
						oldRecipe.getRecipeIngredients().toArray()
				);
				FusionRecipeAPI.addRecipe(newRecipe);
				if (!FusionRecipeAPI.getRecipes().contains(newRecipe)) {
					FusionRecipeAPI.addRecipe(oldRecipe);
					if (!FusionRecipeAPI.getRecipes().contains(oldRecipe)) {
						LogHelper.error("One of the recipes in the fusion crafting database cannot be changed. You must set the Fusion Cost Multiplier to 1 and re-run Minecraft.");
						throw new Error("Fusion Crafting cannot be changed for some reason.  Set the Fusion Cost Multiplier to 1 for Draconic Additions and restart Minecraft.");
					}
				}
				else LogHelper.dev(newRecipe.getRecipeOutput(newRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " now " + newRecipe.getIngredientEnergyCost());
			}
			else {
				if (!oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName().contains("air"))
					LogHelper.dev("Recipe with catalyst " + oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()).getItem().getUnlocalizedName() + " is not compatible.  Skipping... ");
			}
		}
		LogHelper.info("Recipe rebalancing complete. Returning control of Draconic Evolution to it's owner.");
		LogHelper.info("OK, that was just rude");
		DraconicAdditions.logger.log(Level.INFO, "Hey, I've got a job to do, okay? It's not like either of us will remember this next time it happens.");
		LogHelper.info("Fair enough i guess...");
	}
}
