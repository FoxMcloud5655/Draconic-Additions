package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.lib.DESoundHandler;

import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.api.IEnergyProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import scala.Int;

public class TileItemDrainer extends TileChaosHolderBase implements IEnergyProvider, ITickable, IChangeListener {

	private int cooldownRatio = 100000;
	private boolean clientPlayedSound = true;

	public final ManagedInt cooldownTime = register("cooldownTime", new ManagedInt(1)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedInt cooldownTimeRemaining = register("cooldownTimeRemaining", new ManagedInt(0)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedInt fakeCapacity = register("fakeCapacity", new ManagedInt(0)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedBool active = register("active", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool powered = register("powered", new ManagedBool(false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public TileItemDrainer() {
		setInventorySize(1);
		setEnergySyncMode().syncViaContainer();
		setCapacityAndTransfer(Int.MaxValue(), 0, 1000000);
		setShouldRefreshOnBlockChange();
		setMaxChaos(0);
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			if (active.value && !clientPlayedSound) {
				world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, DESoundHandler.boom, SoundCategory.BLOCKS, 1.0F, 2.0F, false);
				clientPlayedSound = true;
			}
			else if (!active.value && clientPlayedSound) {
				clientPlayedSound = false;
			}
			return;
		}
		active.value = cooldownTimeRemaining.value > 2;
		if (cooldownTimeRemaining.value > 0) {
			cooldownTimeRemaining.value -= 1;
		}
		if (cooldownTimeRemaining.value == 0 && cooldownTime.value > 0) {
			cooldownTime.value = 0;
		}
		if (cooldownTimeRemaining.value <= 0 && getEnergyStored() == 0 && !powered.value) {
			extractEnergy();
		}
		energyStorage.modifyEnergyStored(-sendEnergyToAll());
		if (energyStorage.getEnergyStored() == 0 && fakeCapacity.value > 0) {
			fakeCapacity.value = 0;
		}
	}

	public void extractEnergy() {
		if (cooldownTimeRemaining.value > 0 || getEnergyStored() > 0) return;
		ItemStack stack = getStackInSlot(0);
		if (!stack.isEmpty() && stack.getItem() instanceof IEnergyContainerItem) {
			if (ItemNBTHelper.getInteger(stack, "Energy", 0) > 0) {
				int energyToExtract = ItemNBTHelper.getInteger(stack, "Energy", 0);
				cooldownTime.value = energyToExtract / cooldownRatio;
				cooldownTimeRemaining.value = cooldownTime.value;
				fakeCapacity.value = energyToExtract;
				energyStorage.modifyEnergyStored(energyToExtract);
				ItemNBTHelper.setInteger(stack, "Energy", 0);
				/*
				 * if (chaos.value > 0) { burnTime.value = (int)(extractSpeed * (1 +
				 * (chaos.value / 2.0D))); extractTimeRemaining.value = burnTime.value;
				 * chaos.value -= (int)Math.floor(Math.random() * (chaos.value / 8)); }
				 */
			}
		}
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
