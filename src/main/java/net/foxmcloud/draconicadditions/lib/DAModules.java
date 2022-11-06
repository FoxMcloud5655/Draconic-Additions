package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.brandonscore.api.TechLevel.*;
import static com.brandon3055.draconicevolution.api.modules.ModuleTypes.AUTO_FEED;
import static net.foxmcloud.draconicadditions.modules.ModuleTypes.*;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.data.NoData;
import com.brandon3055.draconicevolution.api.modules.lib.BaseModule;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleItem;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.modules.*;
import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.foxmcloud.draconicadditions.modules.data.TickAccelData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DraconicAdditions.MODID, bus = MOD)
@ObjectHolder(DraconicAdditions.MODID)
public class DAModules {	
	@ObjectHolder("chaotic_auto_feed")
	public static Module<NoData> chaoticAutoFeed;

	@ObjectHolder("draconic_tick_accel")
	public static Module<TickAccelData> draconicTickAccel;

	@ObjectHolder("chaotic_tick_accel")
	public static Module<TickAccelData> chaoticTickAccel;
	
	@ObjectHolder("stable_chaos")
	public static Module<StableChaosData> semiStableChaos;
	
	@ObjectHolder("chaos_injector")
	public static Module<ChaosInjectorData> chaosInjector;

	private static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();
	public static transient Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();
	private static transient CyclingItemGroup moduleGroup = new CyclingItemGroup(DraconicAdditions.MODID + ".modules", 40, () -> moduleItemMap.values().toArray(new Item[0]), ITEM_REGISTRY_ORDER);

	private static void registerModules() {
		register(new ModuleImpl<>(AUTO_FEED, CHAOTIC, autoFeedData((float)DAConfig.chaoticFeedAmount)), "chaotic_auto_feed");
		register(new ModuleImpl<>(TICK_ACCEL, DRACONIC, tickAccelData(DAConfig.draconicAccelTicks)), "draconic_tick_accel");
		register(new ModuleImpl<>(TICK_ACCEL, CHAOTIC, tickAccelData(DAConfig.chaoticAccelTicks)), "chaotic_tick_accel");
		register(new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(DAConfig.semiStableChaosMax)), "stable_chaos");
		register(new ModuleImpl<>(CHAOS_INJECTOR, CHAOTIC, chaosInjectorData(DAConfig.chaosInjectorRate)), "chaos_injector");
	}

	private static Function<Module<AutoFeedData>, AutoFeedData> autoFeedData(float defFoodStorage) {
		return e -> {
			float foodStorage = (float) ModuleCfg.getModuleDouble(e, "food_storage", defFoodStorage);
			return new AutoFeedData(foodStorage);
		};
	}

	private static Function<Module<TickAccelData>, TickAccelData> tickAccelData(int defTickSpeed) {
		return e -> {
			int speed = ModuleCfg.getModuleInt(e, "tick_accel", defTickSpeed);
			return new TickAccelData(speed);
		};
	}
	
	private static Function<Module<StableChaosData>, StableChaosData> stableChaosData(int defMaxChaos) {
		return e -> {
			int maxChaos = ModuleCfg.getModuleInt(e, "max_chaos", defMaxChaos);
			return new StableChaosData(maxChaos);
		};
	}
	
	private static Function<Module<ChaosInjectorData>, ChaosInjectorData> chaosInjectorData(int defRate) {
		return e -> {
			int rate = ModuleCfg.getModuleInt(e, "injection_rate", defRate);
			return new ChaosInjectorData(rate);
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
