package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.brandonscore.api.TechLevel.CHAOTIC;
import static com.brandon3055.draconicevolution.api.modules.ModuleTypes.AUTO_FEED;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.data.NoData;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleItem;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.DAContent;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DraconicAdditions.MODID, bus = MOD)
@ObjectHolder(DraconicAdditions.MODID)
public class DAModules {
	private static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();
	public static transient Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();
	private static Supplier<Object[]> tabDAmodules = () -> new Object[]{DAContent.chaosHeart};
	private static transient CyclingItemGroup moduleGroup = new CyclingItemGroup(DraconicAdditions.MODID + ".modules", 40, tabDAmodules, ITEM_REGISTRY_ORDER);

	@ObjectHolder("chaotic_auto_feed")
	public static Module<NoData> chaoticAutoFeed;

	private static void registerModules() {
		//Properties props = new Properties().tab(moduleGroup);
		register(new ModuleImpl<>(AUTO_FEED, CHAOTIC, autoFeedData((float)DAConfig.chaoticFeedAmount)), "chaotic_auto_feed");
	}

	private static Function<Module<AutoFeedData>, AutoFeedData> autoFeedData(float defFoodStorage) {
		return e -> {
			float foodStorage = (float) ModuleCfg.getModuleDouble(e, "food_storage", defFoodStorage);
			return new AutoFeedData(foodStorage);
		};
	}

	private static void register(ModuleImpl<?> module, String name) {
		ModuleItem<?> item = new ModuleItem<>(new Properties().tab(moduleGroup), module);
		item.setRegistryName(name + "_module");
		module.setRegistryName(name);
		module.setModuleItem(item);
		moduleItemMap.put(module, item);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		moduleItemMap.clear();
		registerModules();
		moduleItemMap.keySet().forEach(BaseModule::reloadData);
		moduleItemMap.values().forEach(e -> event.getRegistry().register(e));
		ModuleCfg.saveStateConfig();
	}

	@SubscribeEvent
	public static void registerModules(RegistryEvent.Register<Module<?>> event) {
		moduleItemMap.keySet().forEach(e -> event.getRegistry().register(e));
	}
}
