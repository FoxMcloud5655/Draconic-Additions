package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;

import com.brandon3055.brandonscore.utils.LogHelperBC;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.foxmcloud.draconicadditions.integration.AE2Compat;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public class CommonProxy {
	public void construct() {
		DAConfig.load();
		AE2Compat.init();
		//FusionCostMultiplier.postInit();
	}

	public void commonSetup(FMLCommonSetupEvent event) {
		if (ModList.get().isLoaded("draconicevolution")) {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?");
			LogHelperBC.info("Cant you see im busy over he-");
			LogHelper.info("*KABOOOOOOM!!!*");
			DraconicAdditions.logger.log(Level.INFO, "...  I see I came at a bad time.  I'll just...  Exit over here.");
			if (ModList.get().isLoaded("curios")) {
				DraconicAdditions.logger.log(Level.INFO, "At least I got the curios I came for here.  I'll just pick those up on the way out...");
				DraconicAdditions.logger.log(Level.INFO, "Sorry about this, Brandon's Core.  It's for your own good.");
			}
			else {
				DraconicAdditions.logger.log(Level.INFO, "Uh...  Draconic Evolution, I don't see the curios you're supposed to have.  MISSION ABORT");
				throw new Error("Curios is not loaded.  It is required for Draconic Additions to work.");
			}
		}
		else {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?  Just looking for Draconic Evolution.  Seen 'em around?");
			LogHelperBC.info("No but at least we wont literally die here from his explosions");
			DraconicAdditions.logger.log(Level.INFO, "Wait, really?  He's not here?  I can't do my job if he isn't here...  MISSION ABORT");
			throw new Error("Draconic Evolution is not loaded.  It is required for Draconic Additions to work.");
		}
	}

	public void clientSetup(FMLClientSetupEvent event) {
	}

	public void serverSetup(FMLDedicatedServerSetupEvent event) {
	}
}
