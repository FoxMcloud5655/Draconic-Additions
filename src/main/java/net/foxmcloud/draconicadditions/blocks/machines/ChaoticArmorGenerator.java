package net.foxmcloud.draconicadditions.blocks.machines;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.GUIHandler;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaoticArmorGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class ChaoticArmorGenerator extends ArmorGenerator {

	public ChaoticArmorGenerator() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileChaoticArmorGenerator();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isClientSide) {
			FMLNetworkHandler.openGui(player, DraconicAdditions.instance, GUIHandler.GUIID_ARMOR_GENERATOR, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
