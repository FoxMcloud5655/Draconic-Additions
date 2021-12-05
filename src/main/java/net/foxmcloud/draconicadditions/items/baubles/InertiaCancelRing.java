package net.foxmcloud.draconicadditions.items.baubles;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class InertiaCancelRing extends EnergyBauble {

	public InertiaCancelRing() {}

	@Override
	protected int getCapacity(ItemStack stack) {
		return BaubleStats.INERTIA_RING_BASE_CAPACITY;
	}

	@Override
	protected int getMaxReceive(ItemStack stack) {
		return BaubleStats.INERTIA_RING_MAX_RECIEVE;
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		PlayerEntity player = (PlayerEntity) entity;
		InertiaCancelRing ring = (InertiaCancelRing) stack.getItem();
		if (player.capabilities.isFlying && ring.getEnergyStored(stack) >= BaubleStats.INERTIA_RING_RF_USAGE) {
			ring.modifyEnergy(stack, -BaubleStats.INERTIA_RING_RF_USAGE);
			if (player.moveForward == 0 && player.moveStrafing == 0) {
				player.motionX *= 0.5;
				player.motionZ *= 0.5;
			}
		}
	}
}
