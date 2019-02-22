package net.foxmcloud.draconicadditions.client;

import com.brandon3055.draconicevolution.utils.LogHelper;

import codechicken.lib.texture.TextureUtils;
import net.foxmcloud.draconicadditions.CommonProxy;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.client.keybinding.KeyBindings;
import net.foxmcloud.draconicadditions.client.keybinding.KeyInputHandler;
import net.foxmcloud.draconicadditions.client.model.DAArmorModelHelper;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
    public void preInit(FMLPreInitializationEvent event) {
    	super.preInit(event);
    	MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        OBJLoader.INSTANCE.addDomain(DraconicAdditions.MODID);
        TextureUtils.addIconRegister(new DAArmorModelHelper());
    }
    
	@Override
    public void init(FMLInitializationEvent event) {
    	super.init(event);
    	KeyBindings.init();
    }
}
