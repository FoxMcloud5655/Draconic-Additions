package net.foxmcloud.draconicadditions.items;

import java.awt.TextComponent;
import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.items.ItemEnergyBase;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Hermal extends ItemEnergyBase {
	
	public Hermal() {
		this.setEnergyStats(100, 0, 100);
	}
	
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            setEnergy(stack, getCapacity(stack));
            subItems.add(stack);
        }
    }
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityPersistentItem(world, location, itemstack);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof PlayerEntity) || world.isClientSide) {
			return;
		}
		stack.setTagCompound(null);
	}
	
	@Override
	public int getMaxReceive(ItemStack stack) {
		return 0;
	}
	
	@Override
	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		return 0;
	}
	
    @Override
    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return getCapacity(stack);
    }
    
    @Override
    public int getEnergyStored(ItemStack stack) {
    	return getCapacity(stack);
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
    	return getCapacity(stack);
    }

    @Override
    public void setEnergy(ItemStack stack, int energy) {
    }

    @Override
    public void modifyEnergy(ItemStack container, int modify) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, EnumHand hand) {
		player.sendStatusMessage(new TextComponentString("No."), true);
		return ActionResult.newResult(EnumActionResult.PASS, ItemStack.EMPTY);
	}
}
