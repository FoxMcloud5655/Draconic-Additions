package net.foxmcloud.draconicadditions;

import codechicken.lib.model.ModelRegistryHelper;
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
	}
}
