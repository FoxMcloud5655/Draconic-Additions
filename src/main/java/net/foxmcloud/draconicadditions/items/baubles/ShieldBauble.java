package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;
import com.brandon3055.draconicevolution.items.armor.ICustomArmor;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ShieldBauble extends EnergyBauble implements ICustomArmor {

	private boolean[] canFly = {false, false, false};

	/**
	 * Can be overridden! This method is called when inquiring about how much energy
	 * is needed to regenerate one shield point on this bauble.
	 */
	@Override
	public int getEnergyPerProtectionPoint() {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when Draconic Evolution checks how
	 * much max shielding this bauble provides.
	 */
	@Override
	public float getProtectionPoints(ItemStack arg0) {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when Draconic Evolution checks the
	 * average rate of recovery all equipped armor/baubles can provide. Recovery is
	 * calculated as: {averateRate}% entropy recovered every 5 seconds.
	 */
	@Override
	public float getRecoveryRate(ItemStack arg0) {
		return 0;
	}

	/**
	 * Must be overridden! This method is called when Baubles checks what type of
	 * bauble this item is.
	 */
	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.TRINKET;
	}

	/**
	 * Can be overridden! This method is called when Draconic Evolution checks how
	 * much fire damage can be negated. 0.0 = none, 1.0 = full protection
	 */
	@Override
	public float getFireResistance(ItemStack arg0) {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when inquiring about how much
	 * horizontal flight speed this bauble provides. Note: Does NOT add to existing
	 * flight speed modifiers; Draconic Evolution only sets it based on the highest
	 * one.
	 */
	@Override
	public float getFlightSpeedModifier(ItemStack arg0, PlayerEntity arg1) {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when inquiring about how much
	 * vertical flight speed this bauble provides. Note: Does NOT add to existing
	 * flight speed modifiers; Draconic Evolution only sets it based on the highest
	 * one.
	 */
	@Override
	public float getFlightVModifier(ItemStack arg0, PlayerEntity arg1) {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when inquiring about how much
	 * jumping power this bauble provides. Note: Does NOT add to existing jumping
	 * power modifiers; Draconic Evolution only sets it based on the highest one.
	 */
	@Override
	public float getJumpModifier(ItemStack arg0, PlayerEntity arg1) {
		return 0;
	}

	/**
	 * Can be overridden! This method is called when inquiring about how much land
	 * speed this bauble provides. Note: Does NOT add to existing land speed
	 * modifiers; Draconic Evolution only sets it based on the highest one.
	 */
	@Override
	public float getSpeedModifier(ItemStack arg0, PlayerEntity arg1) {
		return 0;
	}

	/**
	 * DO NOT OVERRIDE! This method is called when inquiring about the flight
	 * capabilities of this bauble. If you need to set flight parameters, change the
	 * "canFly" variable and override the "onEquipped" method. Note: Doing so will
	 * force your parameters over all others.
	 *
	 */
	@Override
	public boolean[] hasFlight(ItemStack arg0) {
		return canFly;
	}

	/**
	 * Can be overridden! This method is called when inquiring if this bauble grants
	 * full-block step-up.
	 *
	 */
	@Override
	public boolean hasHillStep(ItemStack arg0, PlayerEntity arg1) {
		return false;
	}

	/**
	 * Can be overridden! This method is called upon equipping this bauble. Make
	 * sure to call "super.onEquipped()" in your override if you do not wish to make
	 * this bauble grant flight, as it currently contains a hacky fix to get around
	 * DE's method of granting flight.
	 *
	 */
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		ArmorSummery armorSummary = new ArmorSummery().getSummery((PlayerEntity) player);
		canFly = armorSummary.flight;
		super.onEquipped(stack, player);
	}
}
