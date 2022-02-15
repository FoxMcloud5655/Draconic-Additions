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
	public static boolean infusedPotatoRightClick = true;
	public static boolean infusedPotatoDrop = true;
	public static boolean infusedPotatoSmack = true;
	public static boolean potatoBreakOnExhaust = true;
	public static boolean potatoDropOnExhaust = true;
	public static int potatoBaseShieldCapacity = 16;
	public static int potatoShieldRechargeCost = 1000;
	public static double potatoShieldRecovery = 1D;
	public static int potatoMaxRecieve = 0;
	public static int potatoBaseCapacity = 100000;
	public static int potatoUpgradeLevel = 0;
	public static int hermalBaseShieldCapacity = 512;
	public static int hermalShieldRechargeCost = 2000;
	public static double hermalShieldRecovery = 10D;
	public static int hermalMaxRecieve = 0;
	public static int hermalBaseCapacity = 800000000;
	public static int hermalUpgradeLevel = 0;
	public static double chaoticFeedAmount = 1000;

	private static void loadServer() {
		serverTag = config.getTag("Server");
		ConfigTag serverIDTag = serverTag.getTag("serverID")
				.setSyncToClient()
				.setComment("This is a randomly generated ID that clients will use to map config settings from this server.")
				.setDefaultString(UUID.randomUUID().toString());
		serverIDTag.setSyncCallback((tag, type) -> serverID = tag.getString());

		ConfigTag armorTweaks = serverTag.getTag("Armor Tweaks");
		armorTweaks.setComment("These allow you to tweak the stats of the armor found in this mod.");

		armorTweaks.getTag("infusedPotatoRightClick").setSyncToClient().setDefaultBoolean(true)
		.setComment("Allows you to set whether the Infused Potato Armor will transform upon right-click.")
		.setSyncCallback((tag, type) -> infusedPotatoRightClick = tag.getBoolean()
				);

		ConfigTag toolTweaks = serverTag.getTag("Tool Tweaks");
		toolTweaks.setComment("These allow you to tweak the stats of the tools found in this mod.");

		toolTweaks.getTag("armorSpeedLimit").setSyncToClient().setDefaultDouble(16)
		.setComment("")
		.setSyncCallback((tag, type) -> potatoShieldRecovery = tag.getDouble()
				);

		ConfigTag curiosTweaks = serverTag.getTag("Curios Tweaks");
		curiosTweaks.setComment("These allow you to tweak the stats of the curios found in this mod.");

		curiosTweaks.getTag("armorSpeedLimit").setSyncToClient().setDefaultDouble(16)
		.setComment("")
		.setSyncCallback((tag, type) -> potatoShieldRecovery = tag.getDouble()
				);

		ConfigTag moduleTweaks = serverTag.getTag("Module Tweaks");
		moduleTweaks.setComment("Allows you to tweak various settings for Draconic Additions Modules.");

		moduleTweaks.getTag("chaoticFeedAmount").setSyncToClient().setDefaultDouble(1000)
		.setComment("")
		.setSyncCallback((tag, type) -> chaoticFeedAmount = tag.getDouble()
				);

		ConfigTag miscTweaks = serverTag.getTag("Misc Tweaks");
		miscTweaks.setComment("Allows you to tweak various things about Draconic Evolution as a whole, such as fusion crafting power costs.");

		miscTweaks.getTag("armorSpeedLimit").setSyncToClient().setDefaultDouble(16)
		.setComment("")
		.setSyncCallback((tag, type) -> potatoShieldRecovery = tag.getDouble()
				);

		/*
		@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Right Click Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon right-click.", autoSync = true)
		@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Drop Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon dropping it.", autoSync = true)
		@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Smack Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon hitting a mob with it.", autoSync = true)
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Break On Exhaust", comment = "Allows you to disable the Potato Armor breaking when it runs out of RF and shielding.", autoSync = true)
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Drop On Exhaust", comment = "Allows you to disable the Potato Armor dropping it's components when broken.  Requires 'Break On Exhaust' to be true.", autoSync = true)
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Base Shield Capacity", comment = "Allows you to adjust the total shield capacity of a full set of Potato Armor.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "60000000")
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Shield Recharge Cost", comment = "Allows you to adjust the amount of RF that Potato Armor requires to recharge 1 shield point.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Shield Recovery", comment = "Allows you to adjust how fast Potato Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "100")
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Max Recieve", comment = "Allows you to adjust how fast Potato Armor is able to recieve RF/tick.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Base Capacity", comment = "Allows you to adjust how much RF the Potato Armor can hold.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "60000000")
		@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Upgrade Level", comment = "Specifies how far the Potato Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "4")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Base Shield Capacity", comment = "Allows you to adjust the total shield capacity of a full set of Chaotic Armor.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "60000000")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Shield Recharge Cost", comment = "Allows you to adjust the amount of RF that Chaotic Armor requires to recharge 1 shield point.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Shield Recovery", comment = "Allows you to adjust how fast Chaotic Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "100")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Max Recieve", comment = "Allows you to adjust how fast Chaotic Armor is able to recieve RF/tick.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Base Capacity", comment = "Allows you to adjust how much RF the Chaotic Armor can hold.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "60000000")
		@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Upgrade Level", comment = "Specifies how far the Chaotic Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "4")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Base Shield Capacity", comment = "Allows you to adjust the total shield capacity of a full set of Hermal Armor.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "60000000")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Shield Recharge Cost", comment = "Allows you to adjust the amount of RF that Hermal Armor requires to recharge 1 shield point.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Shield Recovery", comment = "Allows you to adjust how fast Hermal Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "100")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Max Recieve", comment = "Allows you to adjust how fast Hermal Armor is able to recieve RF/tick.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Base Capacity", comment = "Allows you to adjust how much RF the Hermal Armor can hold.", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "2147483647")
		@ModConfigProperty(category = "Armor Tweaks", name = "Hermal - Upgrade Level", comment = "Specifies how far the Hermal Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
		@ModConfigProperty.MinMax(min = "0", max = "4")
		 */
	}
}
