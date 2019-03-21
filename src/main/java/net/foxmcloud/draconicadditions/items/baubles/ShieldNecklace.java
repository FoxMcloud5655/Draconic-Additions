package net.foxmcloud.draconicadditions.items.baubles;

import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;

public class ShieldNecklace extends ShieldBauble {

	private int level;

	public ShieldNecklace(int level) {
		this.level = level;
	}

	@Override
	protected int getCapacity(ItemStack stack) {
		return BaubleStats.NECKLACE_BASE_CAPACITY * (int) Math.pow(2, level);
	}

	@Override
	protected int getMaxReceive(ItemStack stack) {
		return BaubleStats.NECKLACE_MAX_RECIEVE * (int) Math.pow(2, level);
	}

	@Override
	public int getEnergyPerProtectionPoint() {
		return BaubleStats.NECKLACE_SHIELD_RECHARGE_COST;
	}

	@Override
	public float getProtectionPoints(ItemStack arg0) {
		return BaubleStats.NECKLACE_BASE_SHIELD_CAPACITY * (int) Math.pow(2, level);
	}

	@Override
	public float getRecoveryRate(ItemStack arg0) {
		return (float) (BaubleStats.NECKLACE_SHIELD_RECOVERY * Math.pow(2, level));
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
}
