package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileArmorGenerator;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;
import net.foxmcloud.draconicadditions.client.gui.GUIArmorGenerator;
import net.foxmcloud.draconicadditions.client.gui.GUIChaoticGenerator;
import net.foxmcloud.draconicadditions.inventory.ContainerArmorGenerator;
import net.foxmcloud.draconicadditions.inventory.ContainerChaoticGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GUIHandler implements IGuiHandler {

    public static final GUIHandler instance = new GUIHandler();

    public static final int GUIID_ARMOR_GENERATOR = 0;
    public static final int GUIID_CHAOTIC_GENERATOR = 1;
    
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
            case GUIID_CHAOTIC_GENERATOR:
            	if (tile instanceof TileChaosLiquefier) {
                    return new ContainerChaoticGenerator(player, (TileChaosLiquefier) tile);
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
            case GUIID_CHAOTIC_GENERATOR:
                if (tile instanceof TileChaosLiquefier) {
                    return new GUIChaoticGenerator(player, (TileChaosLiquefier) tile);
                }
                break;
        }
        return null;
    }
}
