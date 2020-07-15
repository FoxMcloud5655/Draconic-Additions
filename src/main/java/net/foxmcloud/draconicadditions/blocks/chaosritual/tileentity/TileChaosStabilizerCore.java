package net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedDouble;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;
import com.brandon3055.draconicevolution.handlers.DEEventHandler;
import com.brandon3055.draconicevolution.lib.DESoundHandler;
import com.google.common.collect.Lists;

import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.FakePlayer;

public class TileChaosStabilizerCore extends TileInventoryBase implements ITickable, IActivatableTile {

	public final ManagedDouble diameter = register("diameter", new ManagedDouble(1)).syncViaTile().finish();
	public final ManagedDouble intensity = register("intensity", new ManagedDouble(0)).syncViaTile().finish();
	private static final float chaosDamage = 10;
	private static final double suckRadius = 5;
	private static final int delayInMultiblockCheck = 20;
	private double actualDiameter = diameter.value;
	private double actualIntensity = intensity.value;
	private final ManagedInt ritualTicks = register("ritualTicks", new ManagedInt(0)).syncViaTile().finish();
	public final ManagedBool isRitualOngoing = register("isRitualOngoing", new ManagedBool(false)).syncViaTile().finish();
	public final ManagedBool isMultiblock = register("isMultiblock", new ManagedBool(false)).syncViaTile().finish();
	private Random rand = new Random();
	private DamageSource chaosBurst = new DamageSource("chaosBurst").setDamageBypassesArmor();

	public TileChaosStabilizerCore() {
		super();
		this.setInventorySize(1);
		setShouldRefreshOnBlockChange();
	}

	@Override
	public void update() {
		if (DEEventHandler.serverTicks % 10 == 0 && !world.isRemote) {
			checkMultiblock();
			updateBlock();
		}
		if (!isRitualOngoing.value) {
			if (isMultiblock.value) {
				intensity.value = 0.25F;
				List<Entity> suckEntities = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(suckRadius));
				for (Entity e : suckEntities) {
					if (e instanceof EntityItem) {
						EntityItem eItem = (EntityItem) e;
						if (eItem.getItem().getItem() instanceof IChaosItem) {
							if (((IChaosItem) eItem.getItem().getItem()).isChaosStable(eItem.getItem())) {
								continue;
							}
						}
					}
					double dx = (pos.getX() + 0.5D - e.posX);
					double dy = (pos.getY() + 0.5D - e.posY);
					double dz = (pos.getZ() + 0.5D - e.posZ);
					double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (distance < 1.1 && e instanceof EntityItem) {
						EntityItem eItem = (EntityItem) e;
						if (eItem.getItem().getItem() instanceof IChaosItem && getStackInSlot(0).isEmpty()) {
							ItemStack stack = eItem.getItem();
							setInventorySlotContents(0, stack);
							world.removeEntity(e);
							startRitual();
							return;
						}
						else {
							e.motionX -= (e.motionX * (suckRadius) / 1.5);
							e.motionY -= (e.motionY * (suckRadius) / 1.5);
							e.motionZ -= (e.motionZ * (suckRadius) / 1.5);
							playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 1.0F);
						}
					}
					else {
						double limit = 1.0 - (distance / suckRadius);
						if (limit > 0.0D) {
							limit *= limit;
							e.motionX += dx / distance * limit * 0.4;
							e.motionY += dy / distance * limit * 0.4;
							e.motionZ += dz / distance * limit * 0.4;
						}
					}
				}
			}
			else intensity.value = 0;
		}
		else {
			if (isMultiblock.value) {
				ritualTicks.value++;
				List<Entity> suckEntities = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(suckRadius * 2));
				for (Entity e : suckEntities) {
					double dx = (pos.getX() + 0.5D - e.posX);
					double dy = (pos.getY() + 0.5D - e.posY);
					double dz = (pos.getZ() + 0.5D - e.posZ);
					double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (distance < 3.1) {
						if (e instanceof EntityItem) {
							EntityItem eItem = (EntityItem) e;
							if (!(eItem.getItem().getItem() instanceof IChaosItem)) {
								world.removeEntity(e);
							}
							continue;
						}
						else if (e instanceof EntityPlayer) {
							EntityPlayer ePlayer = (EntityPlayer) e;
							if (!(ePlayer instanceof FakePlayer)) {
								ePlayer.attackEntityFrom(chaosBurst, 5);
							}
						}
						else if (e instanceof EntityLiving) {
							((EntityLiving) e).attackEntityFrom(chaosBurst, 20);
						}
					}
					double limit = 1.0 - (distance / (suckRadius * 2));
					if (limit > 0.0D) {
						limit *= limit;
						e.motionX += dx / distance * limit * 0.8D;
						e.motionY += dy / distance * limit * 0.8D;
						e.motionZ += dz / distance * limit * 0.8D;
					}
				}
				if (ritualTicks.value < 260 && DEEventHandler.serverTicks % 10 == 0) {
					playSound(DESoundHandler.sunDialEffect, 1.0F, 0.5F);
				}
				if (ritualTicks.value >= 140 && ritualTicks.value < 300) {
					if (DEEventHandler.serverTicks % 2 == 0 && rand.nextBoolean()) {
						int i = rand.nextInt(4);
						switch (i) {
						case 0: // West Pillar
							world.spawnEntity(new EntityLightningBolt(world, pos.getX() + 2, pos.getY() + 1, pos.getZ(), false));
							break;
						case 1: // East Pillar
							world.spawnEntity(new EntityLightningBolt(world, pos.getX() - 2, pos.getY() + 1, pos.getZ(), false));
							break;
						case 2: // South Pillar
							world.spawnEntity(new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ() + 2, false));
							break;
						case 3: // North Pillar
							world.spawnEntity(new EntityLightningBolt(world, pos.getX(), pos.getY() + 1, pos.getZ() - 2, false));
							break;
						}
					}
					if (ritualTicks.value == 240) {
						intensity.value = 1F;
					}
				}
				if (ritualTicks.value >= 300) {
					if (checkMultiblock()) endRitual(true);
					else endRitual(false);
				}
			}
			else {
				endRitual(false);
			}
		}
	}

	private boolean checkMultiblock() {
		isMultiblock.value = true;
		List<BlockPos> check = new ArrayList<BlockPos>();
		check.add(new BlockPos(pos.getX() + 2, pos.getY(), pos.getZ()));
		check.add(new BlockPos(pos.getX() - 2, pos.getY(), pos.getZ()));
		check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 2));
		check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 2));
		for (BlockPos checkPos : check) {
			if (world.getBlockState(checkPos).getBlock().equals(Block.getBlockFromName("draconicevolution:draconic_block"))) continue;
			isMultiblock.value = false;
			return false;
		}
		check.clear();
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))));
		for (BlockPos checkPos : check) {
			if (checkPos.equals(pos)) continue;
			if (world.isAirBlock(checkPos)) continue;
			isMultiblock.value = false;
			return false;
		}
		check.clear();
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(-2, -2, -2), pos.add(2, -2, 2))));
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(0, -2, -3), pos.add(0, 0, -3))));
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(0, -2, 3), pos.add(0, 0, 3))));
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(-3, -2, 0), pos.add(-3, 0, 0))));
		check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(3, -2, 0), pos.add(3, 0, 0))));
		for (BlockPos checkPos : check) {
			if (world.getBlockState(checkPos).getBlock().equals(Block.getBlockFromName("draconicevolution:infused_obsidian"))) continue;
			isMultiblock.value = false;
			return false;
		}
		return true;
	}

	private BlockPos getOffsetPos(Vec3I vec) {
		return pos.subtract(vec.getPos());
	}

	private Vec3I getOffsetVec(BlockPos offsetPos) {
		return new Vec3I(pos.subtract(offsetPos));
	}

	@Override
	public boolean canRenderBreaking() {
		return true;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return true;
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 40960.0D;
	}

	private void sendMessage(EntityPlayer player, String message) {
		if (!world.isRemote) player.sendStatusMessage(new TextComponentTranslation(message), true);
	}

	private void playSound(SoundEvent sound, float volume, float pitch) {
		if (!world.isRemote) DESoundHandler.playSoundFromServer(world, new Vec3D(pos), sound, SoundCategory.BLOCKS, volume, pitch, false, 64);
	}

	public double getCoreDiameter() {
		if (diameter.value > actualDiameter) {
			actualDiameter += 0.005F;
		}
		else if (diameter.value < actualDiameter) {
			actualDiameter -= 0.005F;
		}

		return actualDiameter;
	}

	public double getCoreIntensity() {
		if (intensity.value > actualIntensity) {
			actualIntensity += 0.001F;
		}
		else if (intensity.value < actualIntensity) {
			actualIntensity -= 0.001F;
		}
		else if (isMultiblock.value && rand.nextInt(10) == 0) {
			intensity.value += rand.nextDouble() / 10;
		}
		return actualIntensity;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (isMultiblock.value || player.isCreative()) {
			ItemStack stack = player.getHeldItem(hand);
			if (!stack.isEmpty() && stack.getCount() > 0) {
				if (getStackInSlot(0).isEmpty()) {
					if (stack.getItem() instanceof IChaosItem) {
						IChaosItem item = (IChaosItem) stack.getItem();
						if (item.isChaosStable(stack)) {
							sendMessage(player, "msg.da.chaosStabilizer.alreadyStabilized");
						}
						else {
							if (player.isCreative()) {
								playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 1.0F, 0.2F);
								item.setChaosStable(stack, true);
							}
							else sendMessage(player, "msg.da.chaosStabilizer.canStabilize");
						}
					}
					else {
						sendMessage(player, "msg.da.chaosStabilizer.cannotStabilize");
					}
				}
				else {
					sendMessage(player, "msg.da.chaosStabilizer.full");
				}
			}
			else {
				if (!getStackInSlot(0).isEmpty()) {
					ItemStack invStack = removeStackFromSlot(0);
					diameter.value = 1;
					intensity.value = 0.25F;
					if (!world.isRemote) {
						player.addItemStackToInventory(invStack);
						markDirty();
					}
				}
				else if (!world.isRemote && !player.isCreative()) {
					ArmorSummery armor = new ArmorSummery().getSummery(player);
					if (armor.maxProtectionPoints > chaosDamage) {
						if (armor.protectionPoints >= armor.maxProtectionPoints / 2) {
							player.attackEntityFrom(new DamageSource("chaosBurst").setDamageBypassesArmor(), (armor.maxProtectionPoints / 2));
						}
						else {
							player.attackEntityFrom(chaosBurst, armor.protectionPoints + (chaosDamage / 4));
						}
					}
					else {
						player.attackEntityFrom(chaosBurst, chaosDamage);
					}
					sendMessage(player, "msg.da.chaosStabilizer.emptyHand");
				}
			}
		}
		else sendMessage(player, "msg.da.chaosStabilizer.notMultiblock");
		return true;
	}

	private void startRitual() {
		if (!isRitualOngoing.value) {
			playSound(SoundEvents.ENTITY_ENDERDRAGON_GROWL, 1.0F, 0.2F);
			isRitualOngoing.value = true;
			diameter.value = 3;
			intensity.value = 0.8F;
			markDirty();
			updateBlock();
		}
	}

	private void endRitual(boolean complete) {
		if (isRitualOngoing.value) {
			isRitualOngoing.value = false;
			ritualTicks.value = 0;
			diameter.value = 1;
			intensity.value = 0.25F;
			ItemStack invStack = removeStackFromSlot(0);
			if (!world.isRemote) {
				if (complete) {
					isMultiblock.value = false;
					List<BlockPos> check = new ArrayList<BlockPos>();
					check.add(new BlockPos(pos.getX() + 2, pos.getY(), pos.getZ()));
					check.add(new BlockPos(pos.getX() - 2, pos.getY(), pos.getZ()));
					check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 2));
					check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 2));
					for (BlockPos checkPos : check) {
						world.setBlockToAir(checkPos);
						world.spawnEntity(new EntityLightningBolt(world, checkPos.getX(), checkPos.getY() + 1, checkPos.getZ(), false));
					}
					((IChaosItem) invStack.getItem()).setChaosStable(invStack, true);
				}
				EntityItem chaosItem = new EntityItem(world, pos.getX(), pos.getY() + 1.01D, pos.getZ(), invStack);
				world.spawnEntity(chaosItem);
				chaosItem.motionX = 0;
				chaosItem.motionY = 0;
				chaosItem.motionZ = 0;
			}
			markDirty();
			updateBlock();
		}
	}
}
