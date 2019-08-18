package net.foxmcloud.draconicadditions.items.armor;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;

import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class ArmorStats {

	@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Right Click Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon right-click.", autoSync = true)
	public static boolean INFUSED_POTATO_RIGHT_CLICK = true;
	@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Drop Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon dropping it.", autoSync = true)
	public static boolean INFUSED_POTATO_DROP = true;
	@ModConfigProperty(category = "Armor Tweaks", name = "Infused Potato - Smack Transform", comment = "Allows you to set whether the Infused Potato Armor will transform upon hitting a mob with it.", autoSync = true)
	public static boolean INFUSED_POTATO_SMACK = true;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Break On Exhaust", comment = "Allows you to disable the Potato Armor breaking when it runs out of RF and shielding.", autoSync = true)
	public static boolean POTATO_BREAK_ON_EXHAUST = true;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Drop On Exhaust", comment = "Allows you to disable the Potato Armor dropping it's components when broken.  Requires 'Break On Exhaust' to be true.", autoSync = true)
	public static boolean POTATO_DROP_ON_EXHAUST = true;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Base Shield Capacity", comment = "Allows you to adjust the total shield capacity of a full set of Potato Armor.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "60000000")
	public static int POTATO_BASE_SHIELD_CAPACITY = 16;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Shield Recharge Cost", comment = "Allows you to adjust the amount of RF that Potato Armor requires to recharge 1 shield point.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int POTATO_SHIELD_RECHARGE_COST = 1000;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Shield Recovery", comment = "Allows you to adjust how fast Potato Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "100")
	public static double POTATO_SHIELD_RECOVERY = 1D;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Max Recieve", comment = "Allows you to adjust how fast Potato Armor is able to recieve RF/tick.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int POTATO_MAX_RECIEVE = 0;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Base Capacity", comment = "Allows you to adjust how much RF the Potato Armor can hold.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "60000000")
	public static int POTATO_BASE_CAPACITY = 100000;
	@ModConfigProperty(category = "Armor Tweaks", name = "Energized Potato - Upgrade Level", comment = "Specifies how far the Potato Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "4")
	public static int POTATO_UPGRADE_LEVEL = 0;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Base Shield Capacity", comment = "Allows you to adjust the total shield capacity of a full set of Chaotic Armor.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "60000000")
	public static int CHAOTIC_BASE_SHIELD_CAPACITY = 1024;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Shield Recharge Cost", comment = "Allows you to adjust the amount of RF that Chaotic Armor requires to recharge 1 shield point.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int CHAOTIC_SHIELD_RECHARGE_COST = 2000;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Shield Recovery", comment = "Allows you to adjust how fast Chaotic Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "100")
	public static double CHAOTIC_SHIELD_RECOVERY = 4D;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Max Recieve", comment = "Allows you to adjust how fast Chaotic Armor is able to recieve RF/tick.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int CHAOTIC_MAX_RECIEVE = 4000000;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Base Capacity", comment = "Allows you to adjust how much RF the Chaotic Armor can hold.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "60000000")
	public static int CHAOTIC_BASE_CAPACITY = 16000000;
	@ModConfigProperty(category = "Armor Tweaks", name = "Chaotic - Upgrade Level", comment = "Specifies how far the Chaotic Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "4")
	public static int CHAOTIC_UPGRADE_LEVEL = 4;
}
