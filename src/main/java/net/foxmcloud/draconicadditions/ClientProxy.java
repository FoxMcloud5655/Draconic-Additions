package net.foxmcloud.draconicadditions;

import codechicken.lib.model.ModelRegistryHelper;
import net.foxmcloud.draconicadditions.client.gui.*;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy extends CommonProxy {

	public static ModelRegistryHelper modelHelper = new ModelRegistryHelper();

	public void construct() {
		super.construct();
	}

	public void commonSetup(FMLCommonSetupEvent event) {
		super.commonSetup(event);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		super.clientSetup(event);
		MenuScreens.register(DAContent.containerChaosLiquefier, GUIChaosLiquefier::new);
		MenuScreens.register(DAContent.containerChaosInfuser, GUIChaosInfuser::new);
	}
}
