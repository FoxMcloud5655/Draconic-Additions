package net.foxmcloud.draconicadditions.blocks.machines;

import java.util.Random;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IRenderOverride;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileCapacitorSupplier;
import net.foxmcloud.draconicadditions.client.render.tile.RenderTileCapacitorSupplier;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CapacitorSupplier extends BlockBCore implements ITileEntityProvider, IRenderOverride {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	public CapacitorSupplier() {
		super(Material.IRON);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
	}
	
	@Override
	public boolean uberIsBlockFullCube() {
        return false;
    }

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileCapacitorSupplier tileCapacitorDischarger = worldIn.getTileEntity(pos) instanceof TileCapacitorSupplier ? (TileCapacitorSupplier) worldIn.getTileEntity(pos) : null;
		return state.withProperty(ACTIVE, tileCapacitorDischarger != null && tileCapacitorDischarger.active.value);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.VALUES[MathHelper.abs(meta % EnumFacing.VALUES.length)];

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCapacitorSupplier();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, PlayerEntity player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isClientSide) {
			TileCapacitorSupplier tileCapacitorDischarger = world.getTileEntity(pos) instanceof TileCapacitorSupplier ? (TileCapacitorSupplier) world.getTileEntity(pos) : null;
			if (tileCapacitorDischarger != null) {
				ItemStack stack = player.getHeldItem(hand);
				if (!stack.isEmpty()) {
					stack = tileCapacitorDischarger.insertItem(stack);
					player.setHeldItem(hand, stack);
				}
				else {
					stack = tileCapacitorDischarger.extractItem();
					player.setHeldItem(hand, stack);
				}
			}
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRenderer(Feature feature) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileCapacitorSupplier.class, new RenderTileCapacitorSupplier());
	}
	
	@Override
	public boolean registerNormal(Feature feature) { return true; }

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getActualState(worldIn, pos).getValue(ACTIVE)) {
			double dx = pos.getX() + 0.5D;
			double dy = pos.getY() + 0.825D;
			double dz = pos.getZ() + 0.5D;
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, dx, dy, dz, 0.0D, 0.1D, 0.0D, new int[0]);
		}
	}
}
