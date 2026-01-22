package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.DraconicAPI;

import net.foxmcloud.draconicadditions.handlers.DAEventHandler;
import net.foxmcloud.draconicadditions.integration.AE2Compat;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DACreativeTabs;
import net.foxmcloud.draconicadditions.lib.DAItemData;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.foxmcloud.draconicadditions.lib.DASounds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(DraconicAdditions.MODID)
public class DraconicAdditions {
	public static final String MODID = "draconicadditions";
	public static final String NAME = "Draconic Additions";
	public static final String VERSION = "${mod_version}";
	public static final String MODID_PREFIX = MODID + ":";
	
	public static Logger logger = LogManager.getLogger(DraconicAdditions.MODID);

	public DraconicAdditions(IEventBus modBus) {
		runChecks();
		DraconicAPI.addModuleProvider(MODID);
		DAConfig.load();
		DAItemData.init(modBus);
		DAContent.init(modBus);
		DAModules.init(modBus);
		DASounds.init(modBus);
		DACreativeTabs.init(modBus);
		AE2Compat.init();
		DAEventHandler.init();
		//FusionCostMultiplier.init();
		Utils.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientInit.init(modBus));
	}
	
	@SuppressWarnings("deprecation")
	private static void runChecks() {
		if (ModList.get().isLoaded("draconicevolution")) {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Co- What have you done!?");
			BrandonsCore.LOGGER.log(Level.INFO, "RUN FOR IT!!!");
			if (ModList.get().isLoaded("curios")) {
				DraconicAdditions.logger.log(Level.INFO, "Not before I grab my Curios!");
				BrandonsCore.LOGGER.log(Level.INFO, "WATEVER MAN");
			}
			else {
				DraconicAdditions.logger.log(Level.INFO, "Not before I-");
				DraconicEvolution.LOGGER.log(Level.ERROR, "***BOOM!***");
				throw new Error("Curios is not loaded.  It is required for Draconic Additions to work.");
			}
		}
		else {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?  Just looking for Draconic Evolution.  Seen 'em around?");
			BrandonsCore.LOGGER.log(Level.INFO, "No but at least we wont literally die from his explosions.");
			DraconicAdditions.logger.log(Level.WARN, "Wait, really?  He's not here?!");
			BrandonsCore.LOGGER.log(Level.INFO, "Not my problem.");
			DraconicAdditions.logger.log(Level.ERROR, "But...  But...  I can't do my job if he doesn't show up!");
			BrandonsCore.LOGGER.log(Level.INFO, "Sorry man cant help you there.");
			throw new Error("Draconic Evolution is not loaded.  It is required for Draconic Additions to work.");
		}
	}
}
