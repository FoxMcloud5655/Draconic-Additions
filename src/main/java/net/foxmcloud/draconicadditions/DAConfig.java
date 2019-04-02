package net.foxmcloud.draconicadditions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.brandon3055.brandonscore.handlers.FileHandler;
import com.brandon3055.brandonscore.registry.IModConfigHelper;
import com.brandon3055.brandonscore.registry.ModConfigContainer;

import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModConfigContainer(modid = DraconicAdditions.MODID)
public class DAConfig implements IModConfigHelper {

	public static Map<String, String> comments = new HashMap<String, String>();

	static {
		comments.put("Armor Tweaks", "These allow you to tweak the stats of the armor found in this mod.");
		comments.put("Tool Tweaks", "These allow you to tweak the stats of the tools found in this mod.");
		comments.put("Bauble Tweaks", "These allow you to tweak the stats of the baubles found in this mod.");
	}

	@Override
	public Configuration createConfiguration(FMLPreInitializationEvent event) {
		return new Configuration(new File(FileHandler.brandon3055Folder, "DraconicAdditions.cfg"), true);
	}

	@Override
	public String getCategoryComment(String category) {
		return comments.getOrDefault(category, "");
	}
	
    @Override
    public void onConfigChanged(String propertyName, String propertyCategory) {
        loadToolStats();
    }
    
    @Override
    public void onConfigLoaded() {
        loadToolStats();
    }
    
    private void loadToolStats() {
    	DAFeatures.chaoticStaffOfPower.loadStatConfig();
    }
	@Mod.EventBusSubscriber
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(DraconicAdditions.MODID)) {
				ConfigManager.sync(DraconicAdditions.MODID, Type.INSTANCE);
			}
		}
	}
}
