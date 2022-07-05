package net.foxmcloud.draconicadditions;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.handlers.DESounds;

import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class CommonMethods {

	public static final DamageSource chaosBurst = new DamageSource("chaosBurst").bypassArmor();
	private static final short gracePeriod = 100;

	//Must be called every tick, else check will fail.
	public static boolean cheatCheck(ItemStack stack, World world) {
		long containerTime = ItemNBTHelper.getLong(stack, "cheatCheck", 0);
		long serverTime = world.getGameTime();
		boolean isCheating = false;
		if (containerTime < serverTime - gracePeriod && containerTime > gracePeriod)  {
			isCheating = true;
		}
		ItemNBTHelper.setLong(stack, "cheatCheck", serverTime);
		return isCheating;
	}

	public static void explodeEntity(Vector3 pos, World world) {
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.beam, SoundCategory.MASTER, 0.25F, 0.5F);
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.fusionComplete, SoundCategory.MASTER, 1.0F, 2.0F);
		if (world.isClientSide) {
			for (int i = 0; i < 5; i++) {
				//BCEffectHandler.spawnFX(DEParticles.ARROW_SHOCKWAVE, world, pos, pos, 128D, 2);
			}
		}
	}

	public static CompoundNBT createFakeNBT(BlockPos pos) {
		CompoundNBT fakeNBT = new CompoundNBT();
		fakeNBT.putInt("x", pos.getX());
		fakeNBT.putInt("y", pos.getY());
		fakeNBT.putInt("z", pos.getZ());
		return fakeNBT;
	}

	public static class BlockStorage {
		private World oldWorld;
		private BlockPos oldPos;
		public BlockState blockState;
		public CompoundNBT tileNBT;

		public BlockStorage(World world, BlockPos pos) {
			storeBlockAt(world, pos);
		}

		public BlockStorage(World world, BlockPos pos, boolean removeBlock) {
			storeBlockAt(world, pos, removeBlock);
		}

		public void storeBlockAt(World world, BlockPos pos) {
			storeBlockAt(world, pos, false);
		}

		public void storeBlockAt(World world, BlockPos pos, boolean removeBlock) {
			oldWorld = world;
			oldPos = pos;
			blockState = world.getBlockState(pos);
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity != null) {
				tileNBT = tileEntity.serializeNBT();
				if (removeBlock) {
					world.getBlockEntity(pos).deserializeNBT(createFakeNBT(pos));
					world.setBlock(pos, Blocks.AIR.defaultBlockState(), BlockFlags.DEFAULT_AND_RERENDER);
				}
			}
		}

		public void restoreBlockAt(World world, BlockPos pos) {
			if (blockState != null) {
				world.setBlock(pos, blockState, BlockFlags.DEFAULT_AND_RERENDER);
				TileEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity != null) {
					tileEntity.deserializeNBT(tileNBT);
					tileEntity.setLevelAndPosition(world, pos);
					tileEntity.requestModelDataUpdate();
				}
			}
		}

		public void restoreBlock() {
			restoreBlockAt(oldWorld, oldPos);
		}

		public CompoundNBT storeBlockInTag(CompoundNBT nbt) {
			nbt.putInt("storedBlockState", Block.getId(blockState));
			if (tileNBT != null) {
				nbt.put("storedTileEntity", tileNBT);
			}
			return nbt;
		}

		public static CompoundNBT storeBlockInTag(World world, BlockPos pos, boolean removeBlock, CompoundNBT nbt) {
			BlockStorage block = new BlockStorage(world, pos, removeBlock);
			nbt.putInt("storedBlockState", Block.getId(block.blockState));
			if (block.tileNBT != null) {
				nbt.put("storedTileEntity", block.tileNBT);
			}
			return nbt;
		}

		public static boolean restoreBlockFromTag(World world, BlockPos pos, CompoundNBT nbt, boolean clearNBT) {
			if (nbt == null || !nbt.contains("storedBlockState")) {
				return false;
			}
			BlockState blockState = Block.stateById(nbt.getInt("storedBlockState"));
			CompoundNBT tileNBT = null;
			if (nbt.contains("storedTileEntity")) {
				tileNBT = (CompoundNBT)nbt.get("storedTileEntity");
			}
			if (blockState == null) {
				return false;
			}
			BlockStorage oldBlock = new BlockStorage(world, pos, true);
			world.setBlock(pos, blockState, BlockFlags.DEFAULT_AND_RERENDER);
			if (world.getBlockState(pos) != blockState) {
				oldBlock.restoreBlock();
				return false;
			}
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity != null) {
				tileEntity.deserializeNBT(tileNBT);
				tileEntity.setLevelAndPosition(world, pos);
				tileEntity.requestModelDataUpdate();
			}
			if (clearNBT) {
				nbt.remove("storedBlockState");
				nbt.remove("storedTileEntity");
			}
			return true;
		}
	}
}