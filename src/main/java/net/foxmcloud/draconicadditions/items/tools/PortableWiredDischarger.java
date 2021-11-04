package net.foxmcloud.draconicadditions.items.tools;

import java.util.ArrayList;
import java.util.List;

import com.brandon3055.brandonscore.items.ItemEnergyBase;
import com.brandon3055.brandonscore.lib.EnergyHelper;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.integration.BaublesHelper;
import com.brandon3055.draconicevolution.integration.ModHelper;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.entity.EntityPlug;
import net.foxmcloud.draconicadditions.lib.DASoundHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PortableWiredDischarger extends PortableWiredCharger {

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			subItems.add(new ItemStack(DAFeatures.pwd, 1, 0));
			subItems.add(new ItemStack(DAFeatures.pwd, 1, 1));
			subItems.add(new ItemStack(DAFeatures.pwd, 1, 2));
			subItems.add(new ItemStack(DAFeatures.pwd, 1, 3));
		}
	}

    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
        int energyReceived = Math.min(getCapacity(stack) - energy, maxReceive);

        if (!simulate) {
            energy += energyReceived;
            ItemNBTHelper.setInteger(stack, "Energy", energy);
        }

        return energyReceived;
    }

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof EntityPlayer) || world.isRemote) {
			return;
		}
		updateActive(stack);
		EntityPlayer player = (EntityPlayer) entity;
		if (active) {
			if (player.getHeldItemMainhand() != stack && player.getHeldItemOffhand() != stack) {
				unplug(stack, player);
			}
			else if (getTileEntity(stack, world) != null) {
				checkDistance(stack, world, player);
				if (getEnergyStored(stack) > 0)
					sendEnergyToSource(stack, world);
			}
			else unplug(stack, player);
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		boolean isPWC = oldStack.getItem() instanceof PortableWiredDischarger && newStack.getItem() instanceof PortableWiredDischarger;
		boolean isSameDamage = oldStack.getItem().getDamage(oldStack) % 4 == newStack.getItem().getDamage(newStack) % 4;
		return !isPWC || !isSameDamage;
	}
}
