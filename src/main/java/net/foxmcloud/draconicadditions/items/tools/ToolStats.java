package net.foxmcloud.draconicadditions.items.tools;

import com.brandon3055.brandonscore.registry.ModConfigContainer;
import com.brandon3055.brandonscore.registry.ModConfigProperty;

import net.foxmcloud.draconicadditions.DraconicAdditions;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class ToolStats {
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaotic Base - RF Capacity", comment = "Allows you to adjust how much RF the Chaotic tools can hold.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "22000000")
	public static int CHAOTIC_BASE_CAPACITY = 20000000;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaotic Base - Mining AOE", comment = "Allows you to adjust the base mining AOE for all Chaotic tools.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "20")
	public static int BASE_CHAOTIC_MINING_AOE = 2;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaotic Staff of Power - Mining Speed", comment = "Allows you to adjust the speed at which the Chaotic Staff of Power mines at.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "500")
	public static double CHAOTIC_STAFF_MINING_SPEED = 120D;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaotic Staff of Power - Attack Damage", comment = "Allows you to adjust the damage that the Chaotic Staff of Power does to mobs.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "1000000")
	public static double CHAOTIC_STAFF_ATTACK_DAMAGE = 120D;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaotic Staff of Power - Attack Speed", comment = "Allows you to adjust the speed at which the Chaotic Staff of Power can attack.", autoSync = true)
	@ModConfigProperty.MinMax(min = "-104", max = "96")
	public static double CHAOTIC_STAFF_ATTACK_SPEED = -2D;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaos Container - Chaos Capacity", comment = "Allows you to adjust how much Liquid Chaos the Chaos Container can store.", autoSync = true)
	@ModConfigProperty.MinMax(min = "1", max = "2147483647")
	public static int CHAOS_CONTAINER_MAX_CHAOS = 1000;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaos Container - RF Capacity", comment = "Allows you to adjust how much RF the Chaos Container can store.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int CHAOS_CONTAINER_MAX_RF = 2000000;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaos Container - RF Recieve", comment = "Allows you to adjust how much RF the Chaos Container can recieve per tick.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int CHAOS_CONTAINER_MAX_TRANSFER = 100000;
	@ModConfigProperty(category = "Tool Tweaks", name = "Chaos Container - RF/Chaos", comment = "Allows you to adjust how much RF is drained per millibucket of Liquid Chaos inside the Chaos Container.", autoSync = true)
	@ModConfigProperty.MinMax(min = "0", max = "2147483647")
	public static int CHAOS_CONTAINER_RF_PER_CHAOS = 10;
}
