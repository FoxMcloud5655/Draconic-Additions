package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.lib.EnergyContainerWrapper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.items.armor.ICustomArmor;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ShieldNecklace extends Item implements ICustomArmor, IBauble {

	private int level;
	private boolean isEquipped = false;
	
	public ShieldNecklace(int level) {
		this.level = level;
	}
	
	@Override
	public int extractEnergy(ItemStack stack, int amount, boolean simulated) {
		return 0;
	}

	@Override
    public int getEnergyStored(ItemStack stack) {
        return ItemNBTHelper.getInteger(stack, "Energy", 0);
    }
	
	protected int getCapacity(ItemStack stack) {
		return BaubleStats.NECKLACE_BASE_CAPACITY * (int)Math.pow(2, level);
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack) {
		return getCapacity(stack);
	}
	
    protected int getMaxReceive(ItemStack stack) {
        return BaubleStats.NECKLACE_MAX_RECIEVE * (int)Math.pow(2, level);
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
	public int getEnergyPerProtectionPoint() {
		return BaubleStats.NECKLACE_SHIELD_RECHARGE_COST;
	}

	@Override
	public float getFireResistance(ItemStack arg0) {
		return 0;
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
	public float getProtectionPoints(ItemStack arg0) {
		return BaubleStats.NECKLACE_BASE_SHIELD_CAPACITY * (int)Math.pow(2, level);
	}

	@Override
	public float getRecoveryRate(ItemStack arg0) {
		return (float)(BaubleStats.NECKLACE_SHIELD_RECOVERY * Math.pow(2, level));
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

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, NBTTagCompound nbt) {
        return new EnergyContainerWrapper(stack);
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack stack) {
        return new EntityPersistentItem(world, location, stack);
    }

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return isEquipped;
	}
	
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		isEquipped = true;
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.75f);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		isEquipped = false;
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
	}
}
