package net.foxmcloud.draconicadditions.lib;

import java.util.List;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import com.brandon3055.draconicevolution.api.fusioncrafting.FusionRecipeAPI;
import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.fusioncrafting.SimpleFusionRecipe;

import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class FusionCostMultiplier {
	@ModConfigProperty(category = "Misc Tweaks", name = "Fusion Crafting Cost Multiplier", comment = "Allows you to adjust the multiplier on fusion crafting.  Mainly intended for modpack authors; this config is NOT automatically updated when you join another server, and you must restart Minecraft for this config to take effect.", requiresMCRestart = true, requiresSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "10")
	public static double POWER_COST_MULTIPLIER = 1.0D;
	
	public static void postInit() {
		List<IFusionRecipe> recipes = FusionRecipeAPI.getRecipes();
		for (IFusionRecipe oldRecipe : recipes) {
			FusionRecipeAPI.removeRecipe(oldRecipe);
			IFusionRecipe newRecipe = new SimpleFusionRecipe(
					oldRecipe.getRecipeOutput(oldRecipe.getRecipeCatalyst()),
					oldRecipe.getRecipeCatalyst(),
					(long)(oldRecipe.getIngredientEnergyCost() * POWER_COST_MULTIPLIER),
					oldRecipe.getRecipeTier(),
					oldRecipe.getRecipeIngredients().toArray()
			);
			FusionRecipeAPI.addRecipe(newRecipe);
		}
	}
}
