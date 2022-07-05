package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;

import com.brandon3055.brandonscore.utils.LogHelperBC;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.foxmcloud.draconicadditions.handlers.DAEventHandler;
import net.foxmcloud.draconicadditions.integration.AE2Compat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public class CommonProxy {
	public void construct() {
		DAConfig.load();
		AE2Compat.init();
		//FusionCostMultiplier.postInit();
		MinecraftForge.EVENT_BUS.register(new DAEventHandler());
	}

	@SuppressWarnings("deprecation")
	public void commonSetup(FMLCommonSetupEvent event) {
		if (ModList.get().isLoaded("draconicevolution")) {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?");
			LogHelperBC.info("Cant you see im busy over he-");
			LogHelper.info("*KABOOOOOOM!!!*");
			DraconicAdditions.logger.log(Level.INFO, "I see you're a little...  Preoccupied, at the moment.");
			if (ModList.get().isLoaded("curios")) {
				DraconicAdditions.logger.log(Level.INFO, "Let me just yoink these curios from Draconic Evolution for my own benefit...");
			}
			else {
				DraconicAdditions.logger.log(Level.ERROR, "Uh...  Where's the curios?  Draconic Evolution, did you blow them up?!");
				throw new Error("Curios is not loaded.  It is required for Draconic Additions to work.");
			}
		}
		else {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?  Just looking for Draconic Evolution.  Seen 'em around?");
			LogHelperBC.info("No but at least we wont literally die from his explosions");
			DraconicAdditions.logger.log(Level.WARN, "Wait, really?  He's not here?!");
			LogHelperBC.info("You heard what i said");
			DraconicAdditions.logger.log(Level.ERROR, "But...  But...  I can't do my job if he doesn't show up!");
			LogHelperBC.info("Sorry man cant help you there.");
			throw new Error("Draconic Evolution is not loaded.  It is required for Draconic Additions to work.");
		}
	}

	public void clientSetup(FMLClientSetupEvent event) {
	}

	public void serverSetup(FMLDedicatedServerSetupEvent event) {
	}
}
