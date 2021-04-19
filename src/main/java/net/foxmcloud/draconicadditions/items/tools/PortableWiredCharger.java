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

public class PortableWiredCharger extends ItemEnergyBase {

	public static final int basicTransfer = 100000;
	public static final int wyvernTransfer = 800000;
	public static final int draconicTransfer = 6400000;
	public static final int chaoticTransfer = 51200000;
	public boolean active = false;
	public double maxDistance = 3;

	public PortableWiredCharger() {
		this.setHasSubtypes(true);
		this.addName(0, "basic").addName(1, "wyvern").addName(2, "draconic").addName(3, "chaotic");
		this.addName(4, "basic").addName(5, "wyvern").addName(6, "draconic").addName(7, "chaotic");
		this.setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab)) {
			subItems.add(new ItemStack(DAFeatures.pwc, 1, 0));
			subItems.add(new ItemStack(DAFeatures.pwc, 1, 1));
			subItems.add(new ItemStack(DAFeatures.pwc, 1, 2));
			subItems.add(new ItemStack(DAFeatures.pwc, 1, 3));
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
	public int getCapacity(ItemStack stack) {
		switch (stack.getItemDamage()) {
		case 0:
		case 4:
			return basicTransfer;
		case 1:
		case 5:
			return wyvernTransfer;
		case 2:
		case 6:
			return draconicTransfer;
		case 3:
		case 7:
			return chaoticTransfer;
		}
		return 0;
	}

	@Override
	public int getMaxReceive(ItemStack stack) {
		return getCapacity(stack);
	}

	@Override
	public int getMaxExtract(ItemStack stack) {
		return getCapacity(stack);
	}

	@Override
	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (ItemNBTHelper.getBoolean(stack, "pluggedIn", false)) {
			unplug(stack, player);
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
		RayTraceResult trace = rayTrace(world, player, false);
		if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && isDistanceValid(trace.getBlockPos(), trace.sideHit, player)) {
			BlockPos pos = trace.getBlockPos();
			if (world.getBlockState(pos).getBlock().hasTileEntity(world.getBlockState(pos))) {
				TileEntity te = world.getTileEntity(pos);
				if (EnergyHelper.isEnergyTile(te, null)) {
					if (EnergyHelper.isEnergyTile(te, trace.sideHit)) {
						Vec3D vec = Vec3D.getCenter(trace.getBlockPos());
						if (!world.isRemote) {
							DASoundHandler.playSoundFromServer(world, vec, DASoundHandler.unplug, SoundCategory.BLOCKS, 0.8F, 1.5F, false, 64.0F);
						}
						ItemNBTHelper.setBoolean(stack, "pluggedIn", true);
						ItemNBTHelper.setInteger(stack, "blockX", vec.floorX());
						ItemNBTHelper.setInteger(stack, "blockY", vec.floorY());
						ItemNBTHelper.setInteger(stack, "blockZ", vec.floorZ());
						ItemNBTHelper.setString(stack, "blockSide", trace.sideHit.getName());
						world.spawnEntity(new EntityPlug(world, player, vec, trace.sideHit));
					}
					else if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation("msg.da.portableWiredCharger.invalidSide"), true);
				}
				else if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation("msg.da.portableWiredCharger.connectEnergySource"), true);
			}
			else if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation("msg.da.portableWiredCharger.connectEnergySource"), true);
		}
		else if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation("msg.da.portableWiredCharger.connectEnergySource"), true);
		return new ActionResult<>(EnumActionResult.PASS, stack);
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
				extractEnergyFromSource(stack, world);
			}
			else unplug(stack, player);
		}
		if (getEnergyStored(stack) > 0) {
			if (ModHelper.isBaublesInstalled) chargeItems(stack, player, getBaubles(player));
			else chargeItems(stack, player, new ArrayList<>());
		}
		if (getEnergyStored(stack) > 0) {
			sendEnergyToSource(stack, world);
		}
	}

	public void chargeItems(ItemStack charger, EntityPlayer player, List<ItemStack> stacks) {
		stacks.addAll(player.inventory.armorInventory);
		stacks.addAll(player.inventory.mainInventory);
		stacks.addAll(player.inventory.offHandInventory);
		for (ItemStack stack : stacks) {
			if (getEnergyStored(charger) == 0) break;
			if (stack.isEmpty()) continue;
			if (EnergyHelper.canReceiveEnergy(stack)) {
				int max = Math.min(getEnergyStored(charger), getMaxExtract(charger));
				int insertedEnergy = EnergyHelper.insertEnergy(stack, Math.min(getMaxExtract(charger), max), false);
				super.extractEnergy(charger, insertedEnergy, false);
			}
		}
	}

	public void extractEnergyFromSource(ItemStack stack, World world) {
		if (active) {
			TileEntity te = getTileEntity(stack, world);
			EnumFacing extractSide = EnumFacing.byName(ItemNBTHelper.getString(stack, "blockSide", "NONE"));
			if (te != null && EnergyHelper.canExtractEnergy(te, extractSide)) {
				int storedEnergy = ItemNBTHelper.getInteger(stack, "Energy", 0);
				int energyToExtract = Math.min(getCapacity(stack) - storedEnergy, EnergyHelper.getEnergyStored(te, extractSide));
				if (energyToExtract == 0) return;
				storedEnergy += EnergyHelper.extractEnergy(te, energyToExtract, extractSide, false);
				ItemNBTHelper.setInteger(stack, "Energy", storedEnergy);
			}
		}
	}

	public void sendEnergyToSource(ItemStack stack, World world) {
		if (active) {
			TileEntity te = getTileEntity(stack, world);
			EnumFacing insertSide = EnumFacing.byName(ItemNBTHelper.getString(stack, "blockSide", "NONE"));
			if (te != null && EnergyHelper.canReceiveEnergy(te, insertSide)) {
				int storedEnergy = ItemNBTHelper.getInteger(stack, "Energy", 0);
				int energyToSend = getCapacity(stack) - storedEnergy;
				if (energyToSend == 0) return;
				storedEnergy -= EnergyHelper.insertEnergy(te, energyToSend, insertSide, false);
				ItemNBTHelper.setInteger(stack, "Energy", storedEnergy);
			}
		}
	}

	public void checkDistance(ItemStack stack, World world, EntityPlayer player) {
		TileEntity te = getTileEntity(stack, world);
		if (te != null) {
			EnumFacing side = EnumFacing.byName(ItemNBTHelper.getString(stack, "blockSide", "NONE"));
			if (!isDistanceValid(te.getPos(), side, player)) {
				unplug(stack, player);
			}
		}
	}

	public boolean isDistanceValid(BlockPos pos, EnumFacing side, EntityPlayer player) {
		BlockPos offset = pos.offset(side).subtract(player.getPosition());
		if (Math.abs(offset.getX()) > maxDistance ||
			Math.abs(offset.getY()) > maxDistance ||
			Math.abs(offset.getZ()) > maxDistance) {
			return false;
		}
		else return true;
	}

	public void unplug(ItemStack stack, EntityPlayer player) {
		if (!player.getEntityWorld().isRemote) DASoundHandler.playSoundFromServer(player.getEntityWorld(), Vec3D.getCenter(player.getPosition()), DASoundHandler.unplug, SoundCategory.BLOCKS, 0.8F, 1.0F, false, 64.0F);
		ItemNBTHelper.setBoolean(stack, "pluggedIn", false);
		ItemNBTHelper.setInteger(stack, "blockX", 0);
		ItemNBTHelper.setInteger(stack, "blockY", 0);
		ItemNBTHelper.setInteger(stack, "blockZ", 0);
		ItemNBTHelper.setString(stack, "blockSide", "NONE");
		updateActive(stack);
	}

	public TileEntity getTileEntity(ItemStack stack, World world) {
		if (active) {
			int x = ItemNBTHelper.getInteger(stack, "blockX", 0);
			int y = ItemNBTHelper.getInteger(stack, "blockY", 0);
			int z = ItemNBTHelper.getInteger(stack, "blockZ", 0);
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			return te;
		}
		return null;
	}

	public void updateActive(ItemStack stack) {
		active = ItemNBTHelper.getBoolean(stack, "pluggedIn", false);
		int newDamage = stack.getItemDamage() % 4 + (active ? 4 : 0);
		if (newDamage != stack.getItemDamage()) stack.setItemDamage(newDamage);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "pluggedIn", false);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		if (active) {
			unplug(stack, player);
		}
		return true;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		boolean isPWC = oldStack.getItem() instanceof PortableWiredCharger && newStack.getItem() instanceof PortableWiredCharger;
		boolean isSameDamage = oldStack.getItem().getDamage(oldStack) % 4 == newStack.getItem().getDamage(newStack) % 4;
		return !isPWC || !isSameDamage;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		if (InfoHelper.holdShiftForDetails(tooltip)) {
			boolean pluggedIn = ItemNBTHelper.getBoolean(stack, "pluggedIn", false);
			tooltip.add("Plugged In: " + pluggedIn);
			if (pluggedIn) {
				tooltip.add("X: " + ItemNBTHelper.getInteger(stack, "blockX", 0));
				tooltip.add("Y: " + ItemNBTHelper.getInteger(stack, "blockY", 0));
				tooltip.add("Z: " + ItemNBTHelper.getInteger(stack, "blockZ", 0));
				tooltip.add("Side: " + ItemNBTHelper.getString(stack, "blockSide", "NONE"));
			}
		}
		InfoHelper.addEnergyInfo(stack, tooltip);
	}

	private static List<ItemStack> getBaubles(EntityPlayer entity) {
		return BaublesHelper.getBaubles(entity);
	}
}
