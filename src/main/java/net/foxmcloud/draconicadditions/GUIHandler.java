package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileArmorGenerator;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileItemDrainer;
import net.foxmcloud.draconicadditions.client.gui.GUIArmorGenerator;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosLiquefier;
import net.foxmcloud.draconicadditions.client.gui.GUIItemDrainer;
import net.foxmcloud.draconicadditions.inventory.ContainerArmorGenerator;
import net.foxmcloud.draconicadditions.inventory.ContainerChaosLiquefier;
import net.foxmcloud.draconicadditions.inventory.ContainerItemDrainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GUIHandler implements IGuiHandler {

	public static final GUIHandler instance = new GUIHandler();

	public static final int GUIID_ARMOR_GENERATOR = 0;
	public static final int GUIID_CHAOS_LIQUEFIER = 1;
	public static final int GUIID_ITEM_DRAINER = 2;

	public static void initialize() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DraconicAdditions.instance, instance);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		switch (ID) {
		case GUIID_ARMOR_GENERATOR:
			if (tile instanceof TileArmorGenerator) {
				return new ContainerArmorGenerator(player, (TileArmorGenerator) tile);
			}
			break;
		case GUIID_CHAOS_LIQUEFIER:
			if (tile instanceof TileChaosLiquefier) {
				return new ContainerChaosLiquefier(player, (TileChaosLiquefier) tile);
			}
			break;
		case GUIID_ITEM_DRAINER:
			if (tile instanceof TileItemDrainer) {
				return new ContainerItemDrainer(player, (TileItemDrainer) tile);
			}
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);
		switch (ID) {
		case GUIID_ARMOR_GENERATOR:
			if (tile instanceof TileArmorGenerator) {
				return new GUIArmorGenerator(player, (TileArmorGenerator) tile);
			}
			break;
		case GUIID_CHAOS_LIQUEFIER:
			if (tile instanceof TileChaosLiquefier) {
				return new GUIChaosLiquefier(player, (TileChaosLiquefier) tile);
			}
			break;
		case GUIID_ITEM_DRAINER:
			if (tile instanceof TileItemDrainer) {
				return new GUIItemDrainer(player, (TileItemDrainer) tile);
			}
			break;
		}
		return null;
	}
}
