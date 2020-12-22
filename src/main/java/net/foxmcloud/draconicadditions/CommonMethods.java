package net.foxmcloud.draconicadditions;

import com.brandon3055.brandonscore.client.particle.BCEffectHandler;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.client.DEParticles;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;
import com.brandon3055.draconicevolution.lib.DESoundHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonMethods {
	
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
	
	public static void explodeEntity(Vec3D pos, World world) {
		world.playSound(pos.x, pos.y, pos.z, DESoundHandler.beam, SoundCategory.MASTER, 0.25F, 0.5F, false);
		world.playSound(pos.x, pos.y, pos.z, DESoundHandler.fusionComplete, SoundCategory.MASTER, 1.0F, 2.0F, false);
		if (world.isRemote) {
			for (int i = 0; i < 5; i++) {
				BCEffectHandler.spawnFX(DEParticles.ARROW_SHOCKWAVE, world, pos, pos, 128D, 2);
			}
		}
	}

	public static void explodeEntity(BlockPos pos, World world) {
		explodeEntity(new Vec3D(pos), world);
	}
}