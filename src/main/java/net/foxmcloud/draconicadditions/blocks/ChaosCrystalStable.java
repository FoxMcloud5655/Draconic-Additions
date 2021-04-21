package net.foxmcloud.draconicadditions.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.draconicevolution.blocks.ChaosCrystal;
import com.brandon3055.draconicevolution.blocks.tileentity.TileChaosCrystal;
import com.brandon3055.draconicevolution.client.render.item.RenderItemReactorComponent;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileChaosCrystal;

import codechicken.lib.model.ModelRegistryHelper;
import net.foxmcloud.draconicadditions.client.render.item.RenderItemChaosCrystal;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChaosCrystalStable extends ChaosCrystal implements IChaosItem {

	public ChaosCrystalStable() {
		super();
		this.setHardness(100.0F);
	}
	
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
    	ItemStack stack = new ItemStack(this);
    	setChaosStable(stack, true);
    	list.add(stack);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    	if (getChaosInfoStable(stack) != null) tooltip.add(getChaosInfoStable(stack));
        super.addInformation(stack, player, tooltip, advanced);
    }

	@Override
	public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
		return 100F;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (!world.isRemote && tile instanceof TileChaosCrystal) {
			TileChaosCrystal tileCrystal = (TileChaosCrystal)tile;
			if (!isChaosStable(stack)) {
				tileCrystal.detonate();
			}
			else {
				tileCrystal.setLockPos();
				tileCrystal.guardianDefeated.value = true;
			}
		}
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderer(Feature feature) {
        super.registerRenderer(feature);
        ModelRegistryHelper.registerItemRenderer(Item.getItemFromBlock(this), new RenderItemChaosCrystal());
    }
}
