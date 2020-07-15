package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileEnergyInventoryBase;
import com.brandon3055.brandonscore.lib.EnergyHelper;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import scala.Int;

public class TileCapacitorSupplier extends TileEnergyInventoryBase implements IEnergyProvider, IEnergyReceiver, ITickable, IChangeListener {

	private int energyToExtract = 0;

	public final ManagedBool active = register("active", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool powered = register("powered", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public TileCapacitorSupplier() {
		setInventorySize(1);
		setEnergySyncMode().syncViaContainer();
		setCapacityAndTransfer(0, 0, 0);
		
		//setShouldRefreshOnBlockChange();
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		if (getStackInSlot(0).isEmpty() && active.value) {
			active.value = false;
		}
		else if (!getStackInSlot(0).isEmpty() && !active.value) {
			active.value = true;
		}
		energyStorage.modifyEnergyStored(-sendEnergyToAll());
	}
	
	public ItemStack insertItem(ItemStack stack) {
		if (!stack.isEmpty()) {
			ItemStack stackInClaws = getStackInSlot(0);
			if (stackInClaws.isEmpty()) {
				if (ItemNBTHelper.getInteger(stack, "Energy", -1) >= 0) {
					IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
					int currentEnergy = item.getEnergyStored(stack);
					int maxEnergy = item.getMaxEnergyStored(stack);
					int transferRate = Math.max(item.extractEnergy(stack, Int.MaxValue(), true), item.receiveEnergy(stack, Int.MaxValue(), true));
					setCapacityAndTransfer(maxEnergy, transferRate, transferRate);
					energyStorage.setEnergyStored(currentEnergy);
					ItemNBTHelper.setInteger(stack, "Energy", 0);
					setInventorySlotContents(0, stack.copy());
					stack = ItemStack.EMPTY;
					markDirty();
					updateBlock();
				}
			}
		}
		return stack;
	}
	
	public ItemStack extractItem() {
		ItemStack stack = ItemStack.EMPTY;
		ItemStack stackInClaws = getStackInSlot(0);
		if (!stackInClaws.isEmpty()) {
			ItemNBTHelper.setInteger(stackInClaws, "Energy", energyStorage.getEnergyStored());
			energyStorage.setEnergyStored(0);
			setCapacityAndTransfer(0, 0, 0);
			stack = removeStackFromSlot(0);
			markDirty();
			updateBlock();
		}
		return stack;
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0 && stack.getItem() instanceof IEnergyContainerItem) {
			return true;
		}
		else return false;
	}

	@Override
	public void onNeighborChange(BlockPos neighbor) {
		powered.value = world.isBlockPowered(pos);
	}
}
