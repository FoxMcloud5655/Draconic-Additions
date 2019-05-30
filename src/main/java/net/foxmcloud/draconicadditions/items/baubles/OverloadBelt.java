package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class OverloadBelt extends BasicBauble {

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		ItemNBTHelper.setBoolean(stack, "Active", false);
		super.onUnequipped(stack, player);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			if (stack.getTagCompound().hasKey("Active")) {
				return stack.getTagCompound().getBoolean("Active");
			}
		}
		return false;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		if (stack.getTagCompound() != null) {
			if (stack.getTagCompound().getBoolean("Active")) {
				if (entity.ticksExisted % 2 == 0) {
					final float damagePercent = 1F;
					EntityPlayer player = (EntityPlayer) entity;
					ArmorSummery summary = new ArmorSummery().getSummery(player);
					if (summary == null || summary.protectionPoints <= 1) {
						ItemNBTHelper.setBoolean(stack, "Active", false);
						player.playSound(SoundEvents.BLOCK_END_GATEWAY_SPAWN, 0.7F, 1.4F);
						return;
					}
					int pointsToSubtract = Math.max((int) Math.ceil(summary.protectionPoints * (damagePercent / 100)), 2);
					float newEntropy = Math.min(summary.entropy + 1 + (pointsToSubtract / 20), 100F);
					float totalAbsorbed = 0;
					int remainingPoints = 0;
					for (int i = 0; i < summary.allocation.length; i++) {
						if (summary.allocation[i] == 0) continue;
						ItemStack armor = summary.armorStacks.get(i);

						float dmgShear = summary.allocation[i] / summary.protectionPoints;
						float dmg = dmgShear * pointsToSubtract;

						float absorbed = Math.min(dmg, summary.allocation[i]);
						totalAbsorbed += absorbed;
						summary.allocation[i] -= absorbed;
						remainingPoints += summary.allocation[i];
						ItemNBTHelper.setFloat(armor, "ProtectionPoints", summary.allocation[i]);
						ItemNBTHelper.setFloat(armor, "ShieldEntropy", newEntropy);
					}

					int strengthCalc = (int) (Math.round(totalAbsorbed) / 2);
					if (strengthCalc > 0) {
						player.removeActivePotionEffect(MobEffects.STRENGTH);
						player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, strengthCalc, true, false));
					}
					else {
						player.removeActivePotionEffect(MobEffects.STRENGTH);
					}
					summary.saveStacks(player);
					player.playSound(SoundEvents.BLOCK_CHORUS_FLOWER_GROW, 0.9F, (float) Math.random() + 0.5F);
				}
			}
		}
		else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("Active", false);
			stack.setTagCompound(nbt);
		}
	}
}
