package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.network.PacketOverloadBelt;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
    	DraconicAdditions.network = NetworkRegistry.INSTANCE.newSimpleChannel(DraconicAdditions.networkChannelName);
    	DraconicAdditions.network.registerMessage(PacketOverloadBelt.Handler.class, PacketOverloadBelt.class, 0, Side.SERVER);
    }
    
    public void init(FMLInitializationEvent event) {
    }
}