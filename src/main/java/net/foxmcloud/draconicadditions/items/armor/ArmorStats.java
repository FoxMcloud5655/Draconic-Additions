package net.foxmcloud.draconicadditions.items.armor;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class ArmorStats {
    
	@ModConfigProperty(category = "Armor Tweaks", name = "potatoBaseShieldCapacity", comment = "Allows you to adjust the total shield capacity of a full set of Potato Armor.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int POTATO_BASE_SHIELD_CAPACITY = 16;
    @ModConfigProperty(category = "Armor Tweaks", name = "potatoShieldRechargeCost", comment = "Allows you to adjust the amount of RF that Potato Armor requires to recharge 1 shield point.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int POTATO_SHIELD_RECHARGE_COST = 100;
    @ModConfigProperty(category = "Armor Tweaks", name = "potatoShieldRecovery", comment = "Allows you to adjust how fast Potato Armor is able to recover entropy.  Value is {this number}% every 5 seconds.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static double POTATO_SHIELD_RECOVERY = 1D;
    @ModConfigProperty(category = "Armor Tweaks", name = "potatoMaxRecieve", comment = "Allows you to adjust how fast Potato Armor is able to recieve RF/tick.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int POTATO_MAX_RECIEVE = 0;
    @ModConfigProperty(category = "Armor Tweaks", name = "potatoBaseCapacity", comment = "Allows you to adjust how much RF the Potato Armor can hold.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int POTATO_BASE_CAPACITY = 10000;
    @ModConfigProperty(category = "Armor Tweaks", name = "potatoUpgradeLevel", comment = "Specifies how far the Potato Armor can be upgraded using Fusion Crafting.\n0 = No Upgrades, 1 = Basic, 2 = Wyvern, 3 = Draconic, 4 = Chaotic", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "4")
    public static int POTATO_UPGRADE_LEVEL = 0;
}
