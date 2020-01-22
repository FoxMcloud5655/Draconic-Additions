package net.foxmcloud.draconicadditions.entity;

import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.foxmcloud.draconicadditions.items.tools.PortableWiredCharger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPlug extends Entity {

	private EntityPlayer player;

	public EntityPlug(World worldIn) {
		super(worldIn);
		this.init(null);
	}

	public EntityPlug(World worldIn, EntityPlayer player, double x, double y, double z, EnumFacing facing) {
		super(worldIn);
		this.init(player);
		this.setPosition(x, y - 0.5, z);
		float yaw;
		switch(facing) {
		case EAST:
			yaw = 90;
			break;
		case WEST:
			yaw = 270;
			break;
		case SOUTH:
			yaw = 180;
			break;
		default:
			yaw = 0;
			break;
		}
		float pitch = facing == EnumFacing.UP ? 90 : facing == EnumFacing.DOWN ? -90 : 0;
		this.setRotation(yaw, pitch);
	}

	public EntityPlug(World worldIn, EntityPlayer player, BlockPos pos, EnumFacing facing) {
		this(worldIn, player, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), facing);
	}

	public EntityPlug(World worldIn, EntityPlayer player, Vec3D vec, EnumFacing facing) {
		this(worldIn, player, vec.x, vec.y, vec.z, facing);
	}

	@Override
	protected void entityInit() {}

	public void onEntityUpdate() {
		this.world.profiler.startSection("entityBaseTick");
		if (this.player != null) {
			ItemStack attachedItemStack = this.player.getHeldItemMainhand() != null ? this.player.getHeldItemMainhand() : this.player.getHeldItemOffhand();
			if (attachedItemStack == ItemStack.EMPTY || !(attachedItemStack.getItem() instanceof PortableWiredCharger)) this.setDead();
			else if (!this.firstUpdate && !ItemNBTHelper.getBoolean(attachedItemStack, "pluggedIn", false)) {
				this.setDead();
			}
		}
		else this.setDead();
		this.firstUpdate = false;
		this.world.profiler.endSection();
	}

	protected void init(EntityPlayer player) {
		this.setSize(1F, 1F);
		this.ignoreFrustumCheck = true;
		this.player = player;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}

	public EntityPlayer getPlayer() {
		return player;
	}

}
