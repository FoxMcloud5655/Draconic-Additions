package net.foxmcloud.draconicadditions.entity;

import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.foxmcloud.draconicadditions.items.tools.PortableWiredCharger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityPlug extends Entity {

	private PlayerEntity player;

	public EntityPlug(World worldIn) {
		super(worldIn);
		this.init(null);
	}

	public EntityPlug(World worldIn, PlayerEntity player, double x, double y, double z, EnumFacing facing) {
		super(worldIn);
		this.init(player);
		float yaw = 0;
		switch(facing) {
		case EAST:
			this.setPosition(x + 0.5, y - 0.5, z);
			yaw = 90;
			break;
		case WEST:
			this.setPosition(x - 0.5, y - 0.5, z);
			yaw = 270;
			break;
		case SOUTH:
			this.setPosition(x, y - 0.5, z + 0.5);
			break;
		case NORTH:
			this.setPosition(x, y - 0.5, z - 0.5);
			yaw = 180;
			break;
		case UP:
			this.setPosition(x, y, z);
			break;
		case DOWN:
			this.setPosition(x, y - 1, z);
			break;
		}
		float pitch = facing == EnumFacing.UP ? 90 : facing == EnumFacing.DOWN ? -90 : 0;
		this.setRotation(yaw, pitch);
	}

	public EntityPlug(World worldIn, PlayerEntity player, BlockPos pos, EnumFacing facing) {
		this(worldIn, player, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), facing);
	}

	public EntityPlug(World worldIn, PlayerEntity player, Vec3D vec, EnumFacing facing) {
		this(worldIn, player, vec.x, vec.y, vec.z, facing);
	}

	@Override
	protected void entityInit() {}

	public void onEntityUpdate() {
		this.world.profiler.startSection("entityBaseTick");
		if (this.player != null) {
			ItemStack stack = this.player.getHeldItemMainhand();
			if (stack.isEmpty() || !(stack.getItem() instanceof PortableWiredCharger)) {
				stack = this.player.getHeldItemOffhand();
			}
			if (stack.isEmpty() || !(stack.getItem() instanceof PortableWiredCharger)) {
				this.setDead();
			}
			else if (!this.firstUpdate && !ItemNBTHelper.getBoolean(stack, "pluggedIn", false)) {
				this.setDead();
			}
		}
		else this.setDead();
		this.firstUpdate = false;
		this.world.profiler.endSection();
	}

	protected void init(PlayerEntity player) {
		this.setSize(1F, 1F);
		this.ignoreFrustumCheck = true;
		this.player = player;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}

	public PlayerEntity getPlayer() {
		return player;
	}

}
