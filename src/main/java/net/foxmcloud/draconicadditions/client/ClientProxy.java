package net.foxmcloud.draconicadditions.client;

import com.brandon3055.draconicevolution.utils.LogHelper;

import codechicken.lib.texture.TextureUtils;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.client.keybinding.KeyBindings;
import net.foxmcloud.draconicadditions.client.model.DAArmorModelHelper;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy {

    public void preInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain(DraconicAdditions.MODID);
        TextureUtils.addIconRegister(new DAArmorModelHelper());
    }
    
    public void init(FMLInitializationEvent event) {
    	KeyBindings.init();
    }
}
