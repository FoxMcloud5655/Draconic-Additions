package net.foxmcloud.draconicadditions.items.baubles;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.EnergyContainerWrapper;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergyBauble extends BasicBauble implements IEnergyContainerItem {

	/**
	 * Can be overridden!
	 * This method is called when inquiring about the max energy this bauble can store.
	 */
	protected int getCapacity(ItemStack stack) {
		return 0;
	}
	
	/**
	 * Can be overridden!
	 * This method is called when inquiring about the max energy this bauble can receive per tick.
	 */
    protected int getMaxReceive(ItemStack stack) {
        return 0;
    }
	
	/**
	 * Can be overridden!
	 * This method is called when inquiring about how much energy can be extracted from this bauble by external sources (dischargers).
	 * Defaults to 0, so if you want other blocks/items to be able to discharge energy, implement your own method for doing so.
	 */
	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
    public int getEnergyStored(ItemStack stack) {
        return ItemNBTHelper.getInteger(stack, "Energy", 0);
    }

	@Override
	public int getMaxEnergyStored(ItemStack stack) {
		return getCapacity(stack);
	}
	
    public void modifyEnergy(ItemStack stack, int modify) {
        int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
        energy += modify;

        if (energy > getCapacity(stack)) {
            energy = getCapacity(stack);
        }
        else if (energy < 0) {
            energy = 0;
        }

        ItemNBTHelper.setInteger(stack, "Energy", energy);
    }
	
    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
        int energyReceived = Math.min(getCapacity(stack) - energy, Math.min(getMaxReceive(stack), maxReceive));

        if (!simulate) {
            energy += energyReceived;
            ItemNBTHelper.setInteger(stack, "Energy", energy);
        }

        return energyReceived;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - ((double) getEnergyStored(stack) / (double) getMaxEnergyStored(stack));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        InfoHelper.addEnergyInfo(stack, tooltip);
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, NBTTagCompound nbt) {
        return new EnergyContainerWrapper(stack);
    }
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "Equipped", false) && getEnergyStored(stack) > 0;
	}
	
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		ItemNBTHelper.setBoolean(stack, "Equipped", true);
		super.onEquipped(stack, player);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		ItemNBTHelper.setBoolean(stack, "Equipped", false);
		super.onUnequipped(stack, player);
	}
}
