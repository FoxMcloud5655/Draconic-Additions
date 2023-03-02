package net.foxmcloud.draconicadditions;

import java.nio.file.Paths;
import java.util.UUID;

import com.brandon3055.draconicevolution.DEConfig;

import codechicken.lib.config.*;

public class DAConfig extends DEConfig {

	private static ConfigCategory config;
	private static ConfigCategory clientTag;
	private static ConfigCategory serverTag;

	public static void load() {
		config = new ConfigFile(DraconicAdditions.MODID)
				.path(Paths.get("./config/brandon3055/DraconicAdditions.cfg"))
				.load();
		loadServer();
		//loadClient();
		config.runSync(ConfigCallback.Reason.MANUAL);
		config.save();
	}

	public static String serverID;

	// Curios

	public static double necklaceCapacityMultiplier = 1;
	public static double harnessCapacityMultiplier = 1;
	public static boolean harnessTickOutOfCuriosSlot = true;

	// Tools

	public static long hermalRFAmount = 10000;
	public static double chaosContainerRFMultiplier = 1;

	// Modules

	public static double chaoticFeedAmount = 1000;
	public static int draconicAccelTicks = 1;
	public static int chaoticAccelTicks = 2;
	public static int semiStableChaosMax = 1000;
	public static int semiStableInstabilityMax = 100;
	public static int stableChaosMax = 1000;
	public static int stableInstabilityMax = 20;
	public static int unstableChaosMax = 5000;
	public static int unstableInstabilityMax = 200;
	public static int chaosInjectorRate = 2;

	// Misc

	public static double fusionCraftingCosts = 1;
	public static boolean enableMemes = false;

	private static void loadServer() {
		serverTag = config.getCategory("Server");
		ConfigValue serverIDTag = serverTag.getValue("serverID")
				.syncTagToClient()
				.setComment("This is a randomly generated ID that clients will use to map config settings from this server.")
				.setDefaultString(UUID.randomUUID().toString());
		serverIDTag.onSync((tag, type) -> serverID = tag.getString());

		// Curios

		ConfigCategory curiosTweaks = serverTag.getCategory("Curios Tweaks");
		curiosTweaks.setComment("These allow you to tweak the stats of the curios found in this mod.");

		curiosTweaks.getValue("necklaceCapacityMultiplier").syncTagToClient().setDefaultDouble(1)
		.setComment("A multiplier to the amount of OP that the Modular Necklace can store.")
		.onSync((tag, type) -> necklaceCapacityMultiplier = tag.getDouble());

		curiosTweaks.getValue("harnessCapacityMultiplier").syncTagToClient().setDefaultDouble(1)
		.setComment("A multiplier to the amount of OP that the Modular Harness can store.")
		.onSync((tag, type) -> harnessCapacityMultiplier = tag.getDouble());

		curiosTweaks.getValue("harnessTickOutOfCuriosSlot").syncTagToClient().setDefaultBoolean(true)
		.setComment("Whether to allow the Modular Harness to tick it's stored machine when it's not equipped.")
		.onSync((tag, type) -> harnessTickOutOfCuriosSlot = tag.getBoolean());

		// Tools

		ConfigCategory toolTweaks = serverTag.getCategory("Tool Tweaks");
		toolTweaks.setComment("These allow you to tweak the stats of the tools found in this mod.");

		toolTweaks.getValue("hermalRFAmount").syncTagToClient().setDefaultLong(10000)
		.setComment("Sets how much RF Hermal provides when powered.")
		.onSync((tag, type) -> hermalRFAmount = tag.getLong());

		toolTweaks.getValue("chaosContainerRFMultiplier").syncTagToClient().setDefaultDouble(1)
		.setComment("Sets how much RF the chaos container should consume per tick for every bucket of chaos.")
		.onSync((tag, type) -> chaosContainerRFMultiplier = tag.getDouble());

		// Modules

		ConfigCategory moduleTweaks = serverTag.getCategory("Module Tweaks");
		moduleTweaks.setComment("Allows you to tweak various settings for Draconic Additions Modules.");

		moduleTweaks.getValue("chaoticFeedAmount").syncTagToClient().setDefaultDouble(1000)
		.setComment("The amount of half-shanks that the Chaotic Auto-Feed Module can store.")
		.onSync((tag, type) -> chaoticFeedAmount = tag.getDouble());

		moduleTweaks.getValue("draconicAccelTicks").syncTagToClient().setDefaultInt(1)
		.setComment("The extra ticks that the Draconic Tick Accelerator provides.  As the number of extra ticks go up, the RF cost increases dramatically.")
		.onSync((tag, type) -> draconicAccelTicks = tag.getInt());

		moduleTweaks.getValue("chaoticAccelTicks").syncTagToClient().setDefaultInt(2)
		.setComment("Same as the above, but for the Chaotic Tick Accelerator.")
		.onSync((tag, type) -> chaoticAccelTicks = tag.getInt());
		
		moduleTweaks.getValue("semiStableChaosMax").syncTagToClient().setDefaultInt(1000)
		.setComment("The maximum amount of liquid chaos each Semi-Stable Chaos Holder can contain.")
		.onSync((tag, type) -> semiStableChaosMax = tag.getInt());
		
		moduleTweaks.getValue("semiStableInstabilityMax").syncTagToClient().setDefaultInt(100)
		.setComment("The maximum amount of entropy each Semi-Stable Chaos Holder can have when modifying the amount of chaos stored.")
		.onSync((tag, type) -> semiStableInstabilityMax = tag.getInt());
		
		moduleTweaks.getValue("stableChaosMax").syncTagToClient().setDefaultInt(1000)
		.setComment("The maximum amount of liquid chaos each Stable Chaos Holder can contain.")
		.onSync((tag, type) -> stableChaosMax = tag.getInt());
		
		moduleTweaks.getValue("stableInstabilityMax").syncTagToClient().setDefaultInt(20)
		.setComment("The maximum amount of entropy each Stable Chaos Holder can have when modifying the amount of chaos stored.")
		.onSync((tag, type) -> stableInstabilityMax = tag.getInt());
		
		moduleTweaks.getValue("unstableChaosMax").syncTagToClient().setDefaultInt(5000)
		.setComment("The maximum amount of liquid chaos each Unstable Chaos Holder can contain.")
		.onSync((tag, type) -> unstableChaosMax = tag.getInt());
		
		moduleTweaks.getValue("unstableInstabilityMax").syncTagToClient().setDefaultInt(200)
		.setComment("The maximum amount of entropy each Unstable Chaos Holder can have when modifying the amount of chaos stored.")
		.onSync((tag, type) -> unstableInstabilityMax = tag.getInt());
		
		moduleTweaks.getValue("chaosInjectorRate").syncTagToClient().setDefaultInt(2)
		.setComment("How many half-hearts the Chaos Injection System can extract/fill every 5 ticks.")
		.onSync((tag, type) -> chaosInjectorRate = tag.getInt());

		// Misc

		ConfigCategory miscTweaks = serverTag.getCategory("Misc Tweaks");
		miscTweaks.setComment("Allows you to tweak various things about Draconic Evolution and Draconic Additions as a whole, such as Fusion Crafting power costs.");

		miscTweaks.getValue("fusionCraftingCosts").syncTagToClient().setDefaultDouble(1)
		.setComment("Allows you to multiply all Fusion Crafting OP costs across both Draconic Evolution and Draconic Additions by this amount.")
		.onSync((tag, type) -> fusionCraftingCosts = tag.getDouble());
		
		miscTweaks.getValue("enableMemes").syncTagToClient().setDefaultBoolean(false)
		.setComment("Enables non-canon content.  Not suited for general play, but content isn't unbalanced.")
		.onSync((tag, type) -> enableMemes = tag.getBoolean());
	}
}
