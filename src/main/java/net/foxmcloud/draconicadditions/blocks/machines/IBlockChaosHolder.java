package net.foxmcloud.draconicadditions.blocks.machines;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.items.ChaosContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockChaosHolder {

	public default boolean tryStoreChaos(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			if (player.getHeldItem(hand).getItem() instanceof ChaosContainer) {
				ItemStack stack = player.getHeldItem(hand);
				if (world.getTileEntity(pos) instanceof TileChaosHolderBase) {
					TileChaosHolderBase tileEntity = (TileChaosHolderBase) world.getTileEntity(pos);
					if (((ChaosContainer) stack.getItem()).getChaos(stack) > 0 && tileEntity.chaos.value != tileEntity.getMaxChaos()) {
						tileEntity.extractChaosFromItem(stack);
					}
					else {
						tileEntity.insertChaosIntoItem(stack);
					}
					return true;
				}
			}
		}
		return false;
	}
}
