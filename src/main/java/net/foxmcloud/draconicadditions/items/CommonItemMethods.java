package net.foxmcloud.draconicadditions.items;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class CommonItemMethods {
	
	public static final DamageSource chaosBurst = new DamageSource("chaosBurst").setDamageBypassesArmor();
	private static final short gracePeriod = 10;
	
	public static float subtractShielding(EntityPlayer player, float damageAmount, float entropyDamageStatic, float entropyDamageFactor) {
		ArmorSummery summary = new ArmorSummery().getSummery(player);
		if (summary == null || summary.protectionPoints <= 1) {
			return 0;
		}
		float newEntropy = Math.min(summary.entropy + entropyDamageStatic + (damageAmount * entropyDamageFactor / 20), 100F);
		float totalAbsorbed = 0;
		int remainingPoints = 0;
		for (int i = 0; i < summary.allocation.length; i++) {
			if (summary.allocation[i] == 0) continue;
			ItemStack armor = summary.armorStacks.get(i);
			float dmgShear = summary.allocation[i] / summary.protectionPoints;
			float dmg = dmgShear * damageAmount;
			float absorbed = Math.min(dmg, summary.allocation[i]);
			totalAbsorbed += absorbed;
			summary.allocation[i] -= absorbed;
			remainingPoints += summary.allocation[i];
			ItemNBTHelper.setFloat(armor, "ProtectionPoints", summary.allocation[i]);
			ItemNBTHelper.setFloat(armor, "ShieldEntropy", newEntropy);
		}
		summary.saveStacks(player);
		return totalAbsorbed;
	}
	
	public static float subtractShielding(EntityPlayer player, float damageAmount) {
		return subtractShielding(player, damageAmount, 1.0F, 1.0F);
	}

	//Must be called every tick, else check will fail.
	public static boolean cheatCheck(ItemStack stack, World world) {
		long containerTime = ItemNBTHelper.getLong(stack, "cheatCheck", 0);
		long serverTime = world.getTotalWorldTime();
		boolean isCheating = false;
		if (containerTime < serverTime - gracePeriod && containerTime != 0)  {
			isCheating = true;
		}
		ItemNBTHelper.setLong(stack, "cheatCheck", serverTime);
		return isCheating;
	}
}