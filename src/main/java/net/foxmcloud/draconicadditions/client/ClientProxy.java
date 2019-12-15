package net.foxmcloud.draconicadditions.client;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.util.ResourceUtils;
import net.foxmcloud.draconicadditions.CommonProxy;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.client.keybinding.KeyBindings;
import net.foxmcloud.draconicadditions.client.keybinding.KeyInputHandler;
import net.foxmcloud.draconicadditions.client.model.DAArmorModelHelper;
import net.foxmcloud.draconicadditions.client.render.entity.RenderPlug;
import net.foxmcloud.draconicadditions.entity.EntityPlug;
import net.foxmcloud.draconicadditions.utils.DATextures;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
		OBJLoader.INSTANCE.addDomain(DraconicAdditions.MODID);
		TextureUtils.addIconRegister(new DAArmorModelHelper());
		TextureUtils.addIconRegister(new DATextures());
		ResourceUtils.registerReloadListener(new DATextures());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlug.class, RenderPlug::new);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		KeyBindings.init();
	}
}
