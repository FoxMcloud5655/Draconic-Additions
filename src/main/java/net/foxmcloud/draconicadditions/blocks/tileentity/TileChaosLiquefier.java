package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.draconicevolution.lib.DESoundHandler;

import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class TileChaosLiquefier extends TileChaosHolderBase implements IEnergyReceiver, ITickable, IChangeListener {

	private int chargeRate = 10000000;
	public int maxCharge = 200;

	public final ManagedInt charge = register("charge", new ManagedInt(0)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedInt chargeTo = register("chargeTo", new ManagedInt(maxCharge)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool active = register("active", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool powered = register("powered", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public TileChaosLiquefier() {
		setInventorySize(1);
		setEnergySyncMode().syncViaContainer();
		setCapacityAndTransfer(2000000000, 20000000, 20000000);
		setShouldRefreshOnBlockChange();
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			if (active.value) {
				if (charge.value >= 0 && charge.value < chargeTo.value - 1) {
					float beamPitch = (1.5F * charge.value / maxCharge) + 0.5F;
					world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, DESoundHandler.beam, SoundCategory.BLOCKS, 0.2F, beamPitch, false);
					// charge.value += 1;
				}
				else {
					world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, DESoundHandler.boom, SoundCategory.BLOCKS, 1.0F, 2.0F, false);
					// charge.value = 0;
				}
			}
		}
		else {
			active.value = charge.value > 0;
			ItemStack stack = getStackInSlot(0);
			if (!stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.value < getMaxChaos()) {
				int finalCharge = calcCharge(stack);
				if (finalCharge != chargeTo.value) {
					chargeTo.value = finalCharge;
				}
				if (energyStorage.getEnergyStored() >= chargeRate) {
					charge.value += 1;
					energyStorage.modifyEnergyStored(-chargeRate);
					if (charge.value >= chargeTo.value) {
						discharge();
					}
				}
				else if (charge.value > 0) {
					charge.value -= 1;
				}
			}
			else if (charge.value > 0) {
				charge.value -= 1;
			}
		}
	}

	public void discharge() {
		ItemStack stack = getStackInSlot(0);
		chaos.value += calcChaos(stack);
		if (chaos.value > getMaxChaos()) {
			chaos.value = getMaxChaos();
		}
		stack.shrink(1);
		if (stack.getCount() == 0) {
			stack = ItemStack.EMPTY;
		}
		charge.value = 0;
	}

	public int calcChaos(ItemStack stack) {
		if (isItemValidForSlot(0, stack)) {
			switch (stack.getItem().getMetadata(stack)) {
			case 0:
				return 1300;
			case 1:
				return 150;
			case 2:
				return 17;
			case 3:
				return 2;
			default:
				return 0;
			}
		}
		else return 0;
	}

	public int calcCharge(ItemStack stack) {
		if (isItemValidForSlot(0, stack)) {
			switch (stack.getItem().getMetadata(stack)) {
			case 0:
				return maxCharge;
			case 1:
				return maxCharge / 2;
			case 2:
				return maxCharge / 4;
			case 3:
				return maxCharge / 8;
			default:
				return maxCharge;
			}
		}
		else return 0;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() == Item.getByNameOrId("draconicevolution:chaos_shard")) {
			return true;
		}
		else if (stack.getItem() == Item.getByNameOrId("draconicadditions:chaos_heart")) {
			return true;
		}
		else return false;
	}

	@Override
	public void onNeighborChange(BlockPos neighbor) {
		powered.value = world.isBlockPowered(pos);
	}
}
