package net.foxmcloud.draconicadditions.integration;

import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

public class AE2Compat {
	public static void init() {
		if (ModList.get().isLoaded("appliedenergistics2")) {
			InterModComms.sendTo("appliedenergistics2", "whitelist-spatial", () -> "net.foxmcloud.draconicadditions.blocks.tileentity.TileArmorGenerator");
			InterModComms.sendTo("appliedenergistics2", "whitelist-spatial", () -> "net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity.TileChaosStabilizerCore");
		}
	}
}
