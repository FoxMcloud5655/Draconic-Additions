package net.foxmcloud.draconicadditions.entity;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.mojang.math.Vector3d;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityPlug extends Entity {

	private Player player;

	public EntityPlug(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
		this.init(null);
	}

	public EntityPlug(EntityType<?> type, Level worldIn, Player player, double x, double y, double z, Direction facing) {
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

	public EntityPlug(EntityType<?> type, Level worldIn, Player player, BlockPos pos, Direction facing) {
		this(type, worldIn, player, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), facing);
	}

	public EntityPlug(EntityType<?> type, Level worldIn, Player player, Vector3d vec, Direction facing) {
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

	protected void init(Player player) {
		//this.setSize(1F, 1F);
		//this.ignoreFrustumCheck = true;
		this.player = player;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {}

	public Player getPlayer() {
		return player;
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public Packet<?> getAddEntityPacket() {
		return null;
	}
}
