package net.foxmcloud.draconicadditions.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.client.particle.BCEffectHandler;
import com.brandon3055.brandonscore.items.ItemEnergyBase;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.api.IInvCharge;
import com.brandon3055.draconicevolution.api.itemupgrade.IUpgradableItem;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.client.DEParticles;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.lib.DESoundHandler;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChaosContainer extends ItemEnergyBase implements IUpgradableItem, IInvCharge {

	private int maxChaos = 1000;
	private int maxRF = 2000000;
	private int maxReceive = 100000;
	private int RFperChaos = 10;

	private DamageSource chaosBurst = new DamageSource("chaosBurst").setDamageBypassesArmor();

	public ChaosContainer() {
		this.setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			subItems.add(new ItemStack(this));
			ItemStack stack = new ItemStack(this);
			setEnergy(stack, getCapacity(stack));
			addChaos(stack, getMaxChaos(stack));
			subItems.add(stack);
		}
	}

	public void addChaos(ItemStack stack, int chaos) {
		ItemNBTHelper.setInteger(stack, "chaos", ItemNBTHelper.getInteger(stack, "chaos", 0) + chaos);
	}

	public void removeChaos(ItemStack stack, int chaos) {
		addChaos(stack, -chaos);
	}

	public int getChaos(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, "chaos", 0);
	}

	public int getMaxChaos(ItemStack stack) {
		int upgrade = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.SHIELD_CAPACITY);
		return maxChaos * (upgrade + 1);
	}

	@Override
	public int getCapacity(ItemStack stack) {
		int upgrade = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.RF_CAPACITY);
		return maxRF * (upgrade + 1);
	}

	@Override
	public int getMaxReceive(ItemStack stack) {
		int upgrade = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.RF_CAPACITY);
		return maxReceive * (upgrade + 1);
	}

	@Override
	public int getMaxExtract(ItemStack stack) {
		return getMaxChaos(stack) * RFperChaos * 2;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return getChaos(stack) > 0;
	}

	@Override
	public List<String> getValidUpgrades(ItemStack stack) {
		return new ArrayList<String>() {
			{
				add(ToolUpgrade.RF_CAPACITY);
				add(ToolUpgrade.SHIELD_CAPACITY);
			}
		};
	}

	@Override
	public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
		return 3;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof EntityPlayer) || world.isRemote) return;
		EntityPlayer player = (EntityPlayer) entity;
		upkeep(player, stack, world);
	}

	public void upkeep(EntityPlayer player, ItemStack stack, World world) {
		if (hasEffect(stack) && !player.isCreative()) {
			int drainedRF = extractEnergy(stack, getChaos(stack) * RFperChaos, false);
			if (drainedRF != getChaos(stack) * RFperChaos) {
				Vec3D pos = new Vec3D(player.posX, player.posY, player.posZ);
				explodeEntity(pos, world);
				player.attackEntityFrom(chaosBurst, getChaos(stack));
				player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosContainer.explode"), true);
				stack.shrink(1);
			}
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		if (getChaos(stack) > 0 && !player.isCreative()) {
			player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosContainer.cantdrop"), true);
			return false;
		}
		else return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (entity instanceof EntityLiving) {
			EntityLiving ent = (EntityLiving) entity;
			if (getChaos(stack) > 0 && !ent.isEntityInvulnerable(chaosBurst)) {
				Vec3D pos = new Vec3D(ent.posX, ent.posY, ent.posZ);
				explodeEntity(pos, player.world);
				if (!player.world.isRemote) {
					float damage = Math.min(getChaos(stack), ent.getHealth());
					entity.attackEntityFrom(chaosBurst, damage);
					removeChaos(stack, (int) Math.floor(damage));
				}
			}
		}
		return false;
	}

	public void explodeEntity(Vec3D pos, World world) {
		world.playSound(pos.x, pos.y, pos.z, DESoundHandler.beam, SoundCategory.MASTER, 0.25F, 0.5F, false);
		world.playSound(pos.x, pos.y, pos.z, DESoundHandler.fusionComplete, SoundCategory.MASTER, 1.0F, 2.0F, false);
		if (world.isRemote) {
			for (int i = 0; i < 5; i++) {
				BCEffectHandler.spawnFX(DEParticles.ARROW_SHOCKWAVE, world, pos, pos, 128D, 2);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("info.da.storedchaos.txt") + ": " + getChaos(stack) + " / " + getMaxChaos(stack) + " mB");
		tooltip.add(I18n.format("info.da.shieldcharge.txt") + ": " + getEnergyStored(stack) + " / " + getMaxEnergyStored(stack) + " RF");
	}

	@Override
	public boolean canCharge(ItemStack stack, EntityPlayer player) {
		return true;
	}
}
