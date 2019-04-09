package net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedDouble;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.DEFeatures.*;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;
import com.brandon3055.draconicevolution.handlers.DEEventHandler;
import com.brandon3055.draconicevolution.lib.DEDamageSources;
import com.google.common.collect.Lists;

import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class TileChaosStabilizerCore extends TileInventoryBase implements ITickable, IActivatableTile {

	public final ManagedDouble diameter = register("diameter", new ManagedDouble(1)).saveToTile().syncViaTile().trigerUpdate().finish();
	public final ManagedDouble intensity = register("intensity", new ManagedDouble(0)).saveToTile().syncViaTile().trigerUpdate().finish();
	private static final float chaosDamage = 10;
	private static final double suckRadius = 5;
	private static final int delayInMultiblockCheck = 20;
	private double actualDiameter = diameter.value;
	private double actualIntensity = intensity.value;
	private int actualDelay = 0;
	private int ritualTicks = 0;
	public final ManagedBool isRitualOngoing = register("isRitualOngoing", new ManagedBool(false)).saveToTile().syncViaTile().trigerUpdate().finish();
	private boolean isMultiblock = false;
	private Random rand = new Random();

	public TileChaosStabilizerCore() {
		super();
		this.setInventorySize(1);
		setShouldRefreshOnBlockChange();
	}

	@Override
	public void update() {
		if (DEEventHandler.serverTicks % 10 == 0) {
			checkMultiblock();
		}
		if (!isRitualOngoing.value) {
			if (isMultiblock) {
				intensity.value = 0.25F;
				List<EntityItem> suckItems = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(suckRadius));
				List<EntityItem> consumeItems = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(0.5D));
				for (EntityItem e : suckItems) {
					if (e.getItem().getItem() instanceof IChaosItem) {
						if (((IChaosItem)e.getItem().getItem()).isChaosStable(e.getItem())) {
							continue;
						}
					}
					double dx = (pos.getX() + 0.5D - e.posX);
					double dy = (pos.getY() + 0.5D - e.posY);
					double dz = (pos.getZ() + 0.5D - e.posZ);
					double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (distance < 1.1) {
						if (e.getItem().getItem() instanceof IChaosItem && getStackInSlot(0) == ItemStack.EMPTY) {
							ItemStack stack = e.getItem();
							setInventorySlotContents(0, stack);
							world.removeEntity(e);
							for (int i = 0; i > 10; i++)
								world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1.0F, 0.2F, false);
							startRitual();
							return;
						}
						else {
							e.motionX -= (e.motionX * (suckRadius) / 1.5);
							e.motionY -= (e.motionY * (suckRadius) / 1.5);
							e.motionZ -= (e.motionZ * (suckRadius) / 1.5);
							world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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
			if (isMultiblock) {
				ritualTicks++;
				if (ritualTicks >= 0 && ritualTicks < 80) {
					//Do nothing, for now.
				}
				else if (ritualTicks >= 80 && ritualTicks < 160) {
					if (DEEventHandler.serverTicks % 4 == 0) {
						world.spawnEntity(new EntityLightningBolt(world, pos.getX() + rand.nextFloat(), pos.getY() + 2.0F, pos.getZ() + rand.nextFloat(), true));
					}
					if (ritualTicks == 120) {
						diameter.value = 4D;
						intensity.value = 0.95F;
					}
				}
				else if (ritualTicks >= 160) {
					endRitual(true);
				}
			}
			else {
				endRitual(false);
			}
		}
	}

	private boolean checkMultiblock() {
		isMultiblock = true;
        List<BlockPos> check = new ArrayList<BlockPos>();
        check.add(new BlockPos(pos.getX() + 2, pos.getY(), pos.getZ()));
        check.add(new BlockPos(pos.getX() - 2, pos.getY(), pos.getZ()));
        check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 2));
        check.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 2));
        for (BlockPos checkPos : check) {
        	if (world.getBlockState(checkPos).getBlock().equals(Block.getBlockFromName("draconicevolution:draconic_block"))) continue;
        	isMultiblock = false;
        	return false;
        }
        check.clear();
        check.addAll(Lists.newArrayList(BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))));
        for (BlockPos checkPos : check) {
        	if (checkPos.equals(pos)) continue;
        	if (world.isAirBlock(checkPos)) continue;
        	isMultiblock = false;
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
		return actualIntensity;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (isMultiblock) {
			ItemStack stack = player.getHeldItem(hand);
			if (!stack.equals(ItemStack.EMPTY) && stack.getCount() > 0) {
				if (getStackInSlot(0) == ItemStack.EMPTY) {
					if (stack.getItem() instanceof IChaosItem) {
						IChaosItem item = (IChaosItem)stack.getItem();
						if (item.isChaosStable(stack)) {
							sendMessage(player, "msg.da.chaosStabilizer.alreadyStabilized");
						}
						else {
							if (player.isCreative()) {
								setInventorySlotContents(0, stack);
								player.inventory.deleteStack(stack);
								startRitual();
								return true;
							}
							sendMessage(player, "msg.da.chaosStabilizer.canStabilize");
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
				if (getStackInSlot(0) != ItemStack.EMPTY) {
					if (player.isCreative() && player.isSneaking()) {
						startRitual();
					}
					else {
						ItemStack invStack = removeStackFromSlot(0);
						diameter.value = 1;
						intensity.value = 0.25F;
						if (!world.isRemote) {
							player.addItemStackToInventory(invStack);
							markDirty();
						}
					}
				}
				else if (!world.isRemote && !player.isCreative()) {
					ArmorSummery armor = new ArmorSummery().getSummery(player);
					if (armor.maxProtectionPoints > chaosDamage) {
						if (armor.protectionPoints >= armor.maxProtectionPoints / 2) {
							player.attackEntityFrom(new DEDamageSources.DamageSourceChaos(player), (armor.maxProtectionPoints/2));
						}
						else {
							player.attackEntityFrom(new DEDamageSources.DamageSourceChaos(player), armor.protectionPoints + (chaosDamage / 4));
						}
					}
					else {
						player.attackEntityFrom(new DEDamageSources.DamageSourceChaos(player), chaosDamage / 4);
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
			isRitualOngoing.value = true;
			diameter.value = 3;
			intensity.value = 0.8F;
			markDirty();
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}

	private void endRitual(boolean complete) {
		if (isRitualOngoing.value) {
			isRitualOngoing.value = false;
			ritualTicks = 0;
			diameter.value = 1;
			intensity.value = 0.25F;
			ItemStack invStack = removeStackFromSlot(0);
			if (!world.isRemote) {
				if (complete) ((IChaosItem)invStack.getItem()).setChaosStable(invStack, true);
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1.55D, pos.getZ(), invStack));
			}
			markDirty();
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		}
	}
}