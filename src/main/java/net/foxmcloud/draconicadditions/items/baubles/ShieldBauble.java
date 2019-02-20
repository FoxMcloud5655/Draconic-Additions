package net.foxmcloud.draconicadditions.items.baubles;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.EnergyContainerWrapper;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.items.armor.ICustomArmor;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShieldBauble extends BasicBauble implements ICustomArmor {
	
	/**
	 * Must be overridden!
	 * This method is called when inquiring about the max energy this bauble can store.
	 */
	protected int getCapacity(ItemStack stack) {
		return 0;
	}
	
	/**
	 * Must be overridden!
	 * This method is called when inquiring about the max energy this bauble can receive per tick.
	 */
    protected int getMaxReceive(ItemStack stack) {
        return 0;
    }
    
	/**
	 * Must be overridden!
	 * This method is called when inquiring about the max energy this bauble can receive per tick.
	 */
	@Override
	public int getEnergyPerProtectionPoint() {
		return 0;
	}
	
	/**
	 * Must be overridden!
	 * This method is called when Draconic Evolution checks how much max shielding this bauble provides.
	 */
	@Override
	public float getProtectionPoints(ItemStack arg0) {
		return 0;
	}

	/**
	 * Must be overridden!
	 * This method is called when Draconic Evolution checks the average rate of recovery all equipped armor/baubles can provide.
	 * Recovery is calculated as: {averateRate}% entropy recovered every 5 seconds.
	 */
	@Override
	public float getRecoveryRate(ItemStack arg0) {
		return 0;
	}
	
	/**
	 * Must be overridden!
	 * This method is called when Baubles checks what type of bauble this item is.
	 */
	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.TRINKET;
	}

	/**
	 * Can be overridden!
	 * This method is called when Draconic Evolution checks how much fire damage can be negated.
	 * 0.0 = none, 1.0 = full protection
	 */
	@Override
	public float getFireResistance(ItemStack arg0) {
		return 0;
	}
    
	@Override
	public int extractEnergy(ItemStack stack, int amount, boolean simulated) {
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
	public float getFlightSpeedModifier(ItemStack arg0, EntityPlayer arg1) {
		return 0;
	}

	@Override
	public float getFlightVModifier(ItemStack arg0, EntityPlayer arg1) {
		return 0;
	}

	@Override
	public float getJumpModifier(ItemStack arg0, EntityPlayer arg1) {
		return 0;
	}

	@Override
	public float getSpeedModifier(ItemStack arg0, EntityPlayer arg1) {
		return 0;
	}

	@Override
	public boolean[] hasFlight(ItemStack arg0) {
		return new boolean[]{false, false, false};
	}

	@Override
	public boolean hasHillStep(ItemStack arg0, EntityPlayer arg1) {
		return false;
	}

	@Override
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
