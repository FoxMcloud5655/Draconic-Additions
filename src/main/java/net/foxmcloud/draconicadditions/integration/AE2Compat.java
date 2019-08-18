package net.foxmcloud.draconicadditions.integration;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class AE2Compat {
    public static void init() {
        if (Loader.isModLoaded("appliedenergistics2")) {
        	FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "net.foxmcloud.draconicadditions.blocks.tileentity.TileArmorGenerator");
        	FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial", "net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity.TileChaosStabilizerCore");
        }
    }
}
