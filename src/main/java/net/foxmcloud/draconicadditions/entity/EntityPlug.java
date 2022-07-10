package net.foxmcloud.draconicadditions.entity;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityPlug extends Entity {

	private PlayerEntity player;

	public EntityPlug(EntityType<?> type, World worldIn) {
		super(type, worldIn);
		this.init(null);
	}

	public EntityPlug(EntityType<?> type, World worldIn, PlayerEntity player, double x, double y, double z, Direction facing) {
		super(type, worldIn);
		this.init(player);
		float yaw = 0;
		switch(facing) {
		case EAST:
			this.setPos(x + 0.5, y - 0.5, z);
			yaw = 90;
			break;
		case WEST:
			this.setPos(x - 0.5, y - 0.5, z);
			yaw = 270;
			break;
		case SOUTH:
			this.setPos(x, y - 0.5, z + 0.5);
			break;
		case NORTH:
			this.setPos(x, y - 0.5, z - 0.5);
			yaw = 180;
			break;
		case UP:
			this.setPos(x, y, z);
			break;
		case DOWN:
			this.setPos(x, y - 1, z);
			break;
		}
		float pitch = facing == Direction.UP ? 90 : facing == Direction.DOWN ? -90 : 0;
		this.setRot(yaw, pitch);
	}

	public EntityPlug(EntityType<?> type, World worldIn, PlayerEntity player, BlockPos pos, Direction facing) {
		this(type, worldIn, player, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), facing);
	}

	public EntityPlug(EntityType<?> type, World worldIn, PlayerEntity player, Vector3d vec, Direction facing) {
		this(type, worldIn, player, vec.x, vec.y, vec.z, facing);
	}

	public void onEntityUpdate() {
		//this.level.profiler.startSection("entityBaseTick");
		if (this.player != null) {
			ItemStack stack = this.player.getMainHandItem();
			if (stack.isEmpty()/* || !(stack.getItem() instanceof PortableWiredCharger)*/) {
				stack = this.player.getOffhandItem();
			}
			if (stack.isEmpty()/* || !(stack.getItem() instanceof PortableWiredCharger)*/) {
				this.kill();
			}
			else if (!this.firstTick && !ItemNBTHelper.getBoolean(stack, "pluggedIn", false)) {
				this.kill();
			}
		}
		else this.kill();
		this.firstTick = false;
		//this.level.profiler.endSection();
	}

	protected void init(PlayerEntity player) {
		//this.setSize(1F, 1F);
		//this.ignoreFrustumCheck = true;
		this.player = player;
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {}

	public PlayerEntity getPlayer() {
		return player;
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}
