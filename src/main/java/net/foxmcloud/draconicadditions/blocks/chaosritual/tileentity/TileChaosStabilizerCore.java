package net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.blocks.TileInventoryBase;
import com.brandon3055.brandonscore.lib.IActivatableTile;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedEnum;
import com.brandon3055.brandonscore.lib.datamanager.ManagedStack;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
import com.brandon3055.brandonscore.lib.datamanager.ManagedVec3I;
import com.brandon3055.brandonscore.utils.FacingUtils;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.GuiHandler;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;
import com.brandon3055.draconicevolution.lib.DEDamageSources;
import com.brandon3055.draconicevolution.lib.DESoundHandler;
import com.brandon3055.draconicevolution.utils.LogHelper;

import codechicken.lib.data.MCDataInput;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileChaosStabilizerCore extends TileInventoryBase implements ITickable, IActivatableTile {
	
	public double diameter = 1;
	public float intensity = 0.25F;
	private static final float chaosDamage = 10;
	private static final double suckRadius = 5;
	private double actualDiameter = diameter;
	private float actualIntensity = intensity;
	private int ritualTicks = 0;
	private boolean isRitualOngoing = false;
    
    public TileChaosStabilizerCore() {
    	super();
        this.setInventorySize(1);
        setShouldRefreshOnBlockChange();
    }

    @Override
    public void update() {
    	if (!isRitualOngoing) {
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
    					intensity = 0;
    					world.removeEntity(e);
    					world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.BLOCKS, 1.0F, 0.2F, false);
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
    	else {
    		ritualTicks++;
    		if (ritualTicks >= 0 && ritualTicks < 80) {
    			//Do nothing, for now.
    		}
    		else if (ritualTicks >= 80 && ritualTicks < 160) {
                world.spawnEntity(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), true));
        		if (ritualTicks == 120) {
        			diameter = 5D;
        			intensity = 0.95F;
        		}
    		}
    		else if (ritualTicks >= 160) {
    			isRitualOngoing = false;
    			ritualTicks = 0;
    			diameter = 1;
    			intensity = 0.25F;
    			ItemStack invStack = removeStackFromSlot(0);
    			if (!world.isRemote) {
    				((IChaosItem)invStack.getItem()).setChaosStable(invStack, true);
    				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1.55D, pos.getZ(), invStack));
    			}
    		}
    	}
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

	public double getCoreDiameter() {
		if (diameter > actualDiameter) {
			actualDiameter += 0.005F;
		}
		else if (diameter < actualDiameter) {
			actualDiameter -= 0.005F;
		}
		return actualDiameter;
	}
	
	public float getCoreIntensity() {
		if (intensity > actualIntensity) {
			actualIntensity += 0.001F;
		}
		else if (intensity < actualIntensity) {
			actualIntensity -= 0.001F;
		}
		return actualIntensity;
	}

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.equals(ItemStack.EMPTY) && stack.getCount() > 0) {
			if (!world.isRemote) {
				if (getStackInSlot(0) == ItemStack.EMPTY) {
					if (stack.getItem() instanceof IChaosItem) {
						IChaosItem item = (IChaosItem)stack.getItem();
						if (item.isChaosStable(stack)) {
							player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosStabilizer.alreadyStabilized"), true);
						}
						else {
							player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosStabilizer.canStabilize"), true);
						}
					}
					else {
						player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosStabilizer.cannotStabilize"), true);
					}
				}
				else {
					player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosStabilizer.full"), true);
				}
			}
		}
		else {
			if (getStackInSlot(0) != ItemStack.EMPTY) {
				if (player.isCreative() && player.isSneaking()) {
					startRitual();
				}
				else {
					ItemStack invStack = removeStackFromSlot(0);
					intensity = 0.25F;
					if (!world.isRemote) {
						player.addItemStackToInventory(invStack);
						markDirty();
					}
				}
			}
			else if (!world.isRemote) {
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
				player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosStabilizer.emptyHand"), true);
			}
		}
		return true;
	}

	private void startRitual() {
		isRitualOngoing = true;
		diameter = 3;
		intensity = 0.8F;
	}
}