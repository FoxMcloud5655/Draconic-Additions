package net.foxmcloud.draconicadditions;

import com.brandon3055.brandonscore.registry.ModFeatureParser;

import net.foxmcloud.draconicadditions.entity.EntityPlug;
import net.foxmcloud.draconicadditions.handlers.DAEventHandler;
import net.foxmcloud.draconicadditions.integration.AE2Compat;
import net.foxmcloud.draconicadditions.lib.DARecipes;
import net.foxmcloud.draconicadditions.lib.FusionCostMultiplier;
import net.foxmcloud.draconicadditions.network.PacketOverloadBelt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		ModFeatureParser.registerModFeatures(DraconicAdditions.MODID);
		DraconicAdditions.network = NetworkRegistry.INSTANCE.newSimpleChannel(DraconicAdditions.networkChannelName);
		DraconicAdditions.network.registerMessage(PacketOverloadBelt.Handler.class, PacketOverloadBelt.class, 0, Side.SERVER);
		MinecraftForge.EVENT_BUS.register(new DAEventHandler());
		EntityRegistry.registerModEntity(new ResourceLocation(DraconicAdditions.MODID, "Plug"), EntityPlug.class, "draconicadditions:plug", 1, DraconicAdditions.instance, 64, 5, false);
	}

	public void init(FMLInitializationEvent event) {
		DARecipes.addRecipes();
		AE2Compat.init();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		FusionCostMultiplier.postInit();
	}
}
