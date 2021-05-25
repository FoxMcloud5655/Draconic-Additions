package net.foxmcloud.draconicadditions.items;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.items.ItemEnergyBase;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;
import com.brandon3055.draconicevolution.lib.RecipeManager;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Hermal extends ItemEnergyBase {

	public Hermal() {
		this.setEnergyStats(DAConfig.HERMAL_RF, 0, DAConfig.HERMAL_RF);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (RecipeManager.isEnabled(DAFeatures.hermal)) {
			if (isInCreativeTab(tab)) {
				ItemStack stack = new ItemStack(this);
				setEnergy(stack, getCapacity(stack));
				subItems.add(stack);
			}
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
		if (!(entity instanceof EntityPlayer) || world.isRemote) {
			return;
		}
		stack.setTagCompound(null);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityLiving;
			player.sendStatusMessage(new TextComponentString("No one contests the power of hermal."), true);
			world.spawnEntity(new EntityLightningBolt(world, player.posX, player.posY + 1, player.posZ, false));
            player.attackEntityFrom(new DamageSource("administrative.kill").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute(), Float.MAX_VALUE);
		}
		stack.shrink(1);
		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.sendStatusMessage(new TextComponentString("This seems like a bad idea..."), true);
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
