package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.draconicevolution.lib.DESoundHandler;

import cofh.redstoneflux.api.IEnergyReceiver;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class TileChaosInfuser extends TileChaosHolderBase implements IEnergyReceiver, ITickable, IChangeListener {

	private int chargeRate = 1000000;
	public int maxCharge = 200;

	public final ManagedBool active = register("active", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool powered = register("powered", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public TileChaosInfuser() {
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
				float beamPitch = (float)(0.5F + (Math.random() * 0.1F));
				world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, DESoundHandler.beam, SoundCategory.BLOCKS, 0.2F, beamPitch, false);
			}
		}
		else {
			ItemStack stack = getStackInSlot(0);
			if (!stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.value > 0) {
				IChaosContainer chaosItem = (IChaosContainer)stack.getItem();
				if (chaosItem.getMaxChaos(stack) > 0 && chaosItem.getChaos(stack) < chaosItem.getMaxChaos(stack) && energyStorage.getEnergyStored() >= chargeRate) {
					active.value = true;
					energyStorage.modifyEnergyStored(-chargeRate);
					chaos.value += chaosItem.addChaos(stack, 1) - 1;
				}
				else active.value = false;
			}
			else active.value = false;
		}
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (stack.getItem() instanceof IChaosContainer) {
			return true;
		}
		else return false;
	}

	@Override
	public void onNeighborChange(BlockPos neighbor) {
		powered.value = world.isBlockPowered(pos);
	}
}
