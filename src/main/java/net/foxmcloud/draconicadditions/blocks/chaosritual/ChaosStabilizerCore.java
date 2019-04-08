package net.foxmcloud.draconicadditions.blocks.chaosritual;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.blocks.BlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IRenderOverride;

import codechicken.lib.model.ModelRegistryHelper;
import net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity.TileChaosStabilizerCore;
import net.foxmcloud.draconicadditions.client.render.item.RenderItemChaosStabilizerCore;
import net.foxmcloud.draconicadditions.client.render.tile.RenderTileChaosStabilizerCore;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChaosStabilizerCore extends BlockBCore implements ITileEntityProvider, IRenderOverride {

    private static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    private static final AxisAlignedBB NO_AABB = new AxisAlignedBB(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);

    public ChaosStabilizerCore() {
    	super();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileChaosStabilizerCore();
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileChaosStabilizerCore) {
            return 200;
        }
        return super.getBlockHardness(blockState, world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileChaosStabilizerCore) {
            return 6000000.0F;
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileChaosStabilizerCore.class, new RenderTileChaosStabilizerCore());
        ModelRegistryHelper.registerItemRenderer(Item.getItemFromBlock(this), new RenderItemChaosStabilizerCore());
    }

    @Override
    public boolean registerNormal(Feature feature) {
        return false;
    }
    
    @Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return FULL_AABB;
    }

    //endregion

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileChaosStabilizerCore) {
            return FULL_AABB;
        }
        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileChaosStabilizerCore)) {
        	super.onBlockExploded(world, pos, explosion);
        }
    }
}