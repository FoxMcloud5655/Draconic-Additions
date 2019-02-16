package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;
import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class BaubleStats {
    
	@ModConfigProperty(category = "Bauble Tweaks", name = "necklaceBaseShieldCapacity", comment = "Allows you to adjust the shield capacity of the basic necklace.  Note: This value doubles every upgrade.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int NECKLACE_BASE_SHIELD_CAPACITY = 4;
    @ModConfigProperty(category = "Bauble Tweaks", name = "necklaceShieldRechargeCost", comment = "Allows you to adjust the amount of RF that necklaces require to recharge 1 shield point.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int NECKLACE_SHIELD_RECHARGE_COST = 500;
    @ModConfigProperty(category = "Bauble Tweaks", name = "necklaceShieldRecovery", comment = "Allows you to adjust how fast the basic necklace is able to recover entropy.  Value is {this number}% every 5 seconds.  Note: This value doubles every upgrade.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static double NECKLACE_SHIELD_RECOVERY = 1D;
    @ModConfigProperty(category = "Bauble Tweaks", name = "necklaceMaxRecieve", comment = "Allows you to adjust how fast the basic necklace is able to recieve RF/tick.  Note: This value doubles every upgrade.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int NECKLACE_MAX_RECIEVE = 500;
    @ModConfigProperty(category = "Bauble Tweaks", name = "necklaceBaseCapacity", comment = "Allows you to adjust how much RF the basic necklace can hold.  Note: This value doubles every upgrade.", autoSync = true)
    @ModConfigProperty.MinMax(min = "0", max = "2147483647")
    public static int NECKLACE_BASE_CAPACITY = 10000;
}
