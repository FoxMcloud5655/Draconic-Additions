package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brandon3055.brandonscore.registry.ModFeatureParser;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.foxmcloud.draconicadditions.client.ClientProxy;
import net.foxmcloud.draconicadditions.lib.DARecipes;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = DraconicAdditions.MODID, name = DraconicAdditions.NAME, version = DraconicAdditions.VERSION, guiFactory = DraconicAdditions.GUI_FACTORY, dependencies = "required-after:draconicevolution;required-after:baubles")
public class DraconicAdditions {
	public static final String MODID = "draconicadditions";
	public static final String NAME = "Draconic Additions";
	public static final String PROXY_COMMON = "net.foxmcloud.draconicadditions.CommonProxy";
	public static final String PROXY_CLIENT = "net.foxmcloud.draconicadditions.client.ClientProxy";
	public static final String VERSION = "1.0";
	public static final String GUI_FACTORY = "net.foxmcloud.draconicadditions.DAGuiFactory";
	public static final String MODID_PREFIX = MODID + ":";
	public static final String networkChannelName = "DAdditionsNC";
	public static SimpleNetworkWrapper network;

	private static Logger logger = LogManager.getLogger(DraconicAdditions.MODID);

	@Mod.Instance(DraconicAdditions.MODID)
	public static DraconicAdditions instance;

	@SidedProxy(clientSide = DraconicAdditions.PROXY_CLIENT, serverSide = DraconicAdditions.PROXY_COMMON)
	public static CommonProxy proxy;

	public DraconicAdditions() {
		if (Loader.isModLoaded("draconicevolution")) {
			logger.log(Level.INFO, "I see you, Draconic Evolution...  Ready for a boost?");
			LogHelper.info("Upping the potential for my draconic arsonal?  You bet!!!");
			if (Loader.isModLoaded("baubles")) {
				logger.log(Level.INFO, "Then let's do this!");
				logger.log(Level.INFO, "Hello Minecraft!!!");
			}
			else {
				logger.log(Level.INFO, "Then let's d- Wait a second, I don't see Baubles...  ABORT ABORT ABORT!");
				throw new Error("Baubles is not loaded.  It is required for Draconic Additions to work.");
			}

		}
		else {
			logger.log(Level.INFO, "Wait a second, I don't see Draconic Evolution...  ABORT ABORT ABORT!");
			throw new Error("Draconic Evolution is not loaded.  It is required for Draconic Additions to work.");
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModFeatureParser.registerModFeatures(MODID);
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		DARecipes.addRecipes();
		proxy.init(event);
	}
}
