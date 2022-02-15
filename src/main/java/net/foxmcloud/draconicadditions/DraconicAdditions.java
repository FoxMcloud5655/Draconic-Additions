package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brandon3055.brandonscore.utils.LogHelperBC;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.event.EventNetworkChannel;

@SuppressWarnings("deprecation")
@Mod(DraconicAdditions.MODID)
public class DraconicAdditions {
	public static final String MODID = "draconicadditions";
	public static final String NAME = "Draconic Additions";
	public static final String VERSION = "${mod_version}";
	public static final String MODID_PREFIX = MODID + ":";

	public static Logger logger = LogManager.getLogger(DraconicAdditions.MODID);
	public static CommonProxy proxy;

	public DraconicAdditions() {
		proxy = DistExecutor.safeRunForDist(() -> CommonProxy::new, () -> CommonProxy::new);
		proxy.construct();
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		proxy.commonSetup(event);
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		proxy.clientSetup(event);
	}

	@SubscribeEvent
	public void onServerSetup(FMLDedicatedServerSetupEvent event) {
		proxy.serverSetup(event);
	}
}
