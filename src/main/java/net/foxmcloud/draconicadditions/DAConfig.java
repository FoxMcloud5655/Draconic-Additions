package net.foxmcloud.draconicadditions;

import java.nio.file.Paths;
import java.util.UUID;

import com.brandon3055.draconicevolution.DEConfig;

import codechicken.lib.config.ConfigTag;
import codechicken.lib.config.StandardConfigFile;

public class DAConfig extends DEConfig {

	private static ConfigTag config;
	private static ConfigTag clientTag;
	private static ConfigTag serverTag;

	public static void load() {
		config = new StandardConfigFile(Paths.get("./config/brandon3055/DraconicAdditions.cfg")).load();
		loadServer();
		//loadClient();
		config.runSync();
		config.save();
	}

	public static String serverID;

	// Curios

	public static double necklaceCapacityMultiplier = 1;
	public static double harnessCapacityMultiplier = 1;
	public static boolean harnessTickOutOfCuriosSlot = true;

	// Tools

	public static long hermalRFAmount = 1000;
	public static double chaosContainerRFMultiplier = 1;

	// Modules

	public static double chaoticFeedAmount = 1000;
	public static int draconicAccelTicks = 1;
	public static int chaoticAccelTicks = 2;
	
	// Misc
	
	public static boolean enableMemes = false;

	private static void loadServer() {
		serverTag = config.getTag("Server");
		ConfigTag serverIDTag = serverTag.getTag("serverID")
				.setSyncToClient()
				.setComment("This is a randomly generated ID that clients will use to map config settings from this server.")
				.setDefaultString(UUID.randomUUID().toString());
		serverIDTag.setSyncCallback((tag, type) -> serverID = tag.getString());

		// Curios

		ConfigTag curiosTweaks = serverTag.getTag("Curios Tweaks");
		curiosTweaks.setComment("These allow you to tweak the stats of the curios found in this mod.");

		curiosTweaks.getTag("necklaceCapacityMultiplier").setSyncToClient().setDefaultDouble(1)
		.setComment("A multiplier to the amount of OP that the Modular Necklace can store.")
		.setSyncCallback((tag, type) -> necklaceCapacityMultiplier = tag.getDouble());

		curiosTweaks.getTag("harnessCapacityMultiplier").setSyncToClient().setDefaultDouble(1)
		.setComment("A multiplier to the amount of OP that the Modular Harness can store.")
		.setSyncCallback((tag, type) -> harnessCapacityMultiplier = tag.getDouble());

		curiosTweaks.getTag("harnessTickOutOfCuriosSlot").setSyncToClient().setDefaultBoolean(true)
		.setComment("Whether to allow the Modular Harness to tick it's stored machine when it's not equipped.")
		.setSyncCallback((tag, type) -> harnessTickOutOfCuriosSlot = tag.getBoolean());

		// Tools

		ConfigTag toolTweaks = serverTag.getTag("Tool Tweaks");
		toolTweaks.setComment("These allow you to tweak the stats of the tools found in this mod.");
		
		toolTweaks.getTag("hermalRFAmount").setSyncToClient().setDefaultLong(1000)
		.setComment("Sets how much RF Hermal provides when powered.")
		.setSyncCallback((tag, type) -> hermalRFAmount = tag.getLong());
		
		toolTweaks.getTag("chaosContainerRFMultiplier").setSyncToClient().setDefaultDouble(1)
		.setComment("Sets how much RF the chaos container should consume per tick for every bucket of chaos.")
		.setSyncCallback((tag, type) -> chaosContainerRFMultiplier = tag.getDouble());

		// Modules

		ConfigTag moduleTweaks = serverTag.getTag("Module Tweaks");
		moduleTweaks.setComment("Allows you to tweak various settings for Draconic Additions Modules.");

		moduleTweaks.getTag("chaoticFeedAmount").setSyncToClient().setDefaultDouble(1000)
		.setComment("The amount of half-shanks that the Chaotic Auto-Feed Module can store.")
		.setSyncCallback((tag, type) -> chaoticFeedAmount = tag.getDouble());

		moduleTweaks.getTag("draconicAccelTicks").setSyncToClient().setDefaultInt(1)
		.setComment("The extra ticks that the Draconic Tick Accelerator provides.  As the number of extra ticks go up, the RF cost increases dramatically.")
		.setSyncCallback((tag, type) -> draconicAccelTicks = tag.getInt());

		moduleTweaks.getTag("chaoticAccelTicks").setSyncToClient().setDefaultInt(2)
		.setComment("Same as the above, but for the Chaotic Tick Accelerator.")
		.setSyncCallback((tag, type) -> chaoticAccelTicks = tag.getInt());

		// Misc

		ConfigTag miscTweaks = serverTag.getTag("Misc Tweaks");
		miscTweaks.setComment("Allows you to tweak various things about Draconic Evolution and Draconic Additions as a whole, such as fusion crafting power costs.");
		
		miscTweaks.getTag("enableMemes").setSyncToClient().setDefaultBoolean(false)
		.setComment("Enables non-canon content.  Not suited for general play, but content isn't unbalanced.")
		.setSyncCallback((tag, type) -> enableMemes = tag.getBoolean());
	}
}
