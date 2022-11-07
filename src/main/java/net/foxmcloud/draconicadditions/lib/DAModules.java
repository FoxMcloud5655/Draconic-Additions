package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.brandonscore.api.TechLevel.*;
import static com.brandon3055.draconicevolution.api.modules.ModuleTypes.AUTO_FEED;
import static net.foxmcloud.draconicadditions.DAConfig.*;
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
	
	@ObjectHolder("semi_stable_chaos")
	public static Module<StableChaosData> semiStableChaos;
	
	@ObjectHolder("stable_chaos")
	public static Module<StableChaosData> stableChaos;
	
	@ObjectHolder("unstable_chaos")
	public static Module<StableChaosData> unstableChaos;
	
	@ObjectHolder("chaos_injector")
	public static Module<ChaosInjectorData> chaosInjector;

	private static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();
	public static transient Map<BaseModule<?>, Item> moduleItemMap = new LinkedHashMap<>();
	private static transient CyclingItemGroup moduleGroup = new CyclingItemGroup(DraconicAdditions.MODID + ".modules", 40, () -> moduleItemMap.values().toArray(new Item[0]), ITEM_REGISTRY_ORDER);

	private static void registerModules() {
		register(new ModuleImpl<>(AUTO_FEED, CHAOTIC, autoFeedData((float)chaoticFeedAmount)), "chaotic_auto_feed");
		register(new ModuleImpl<>(TICK_ACCEL, DRACONIC, tickAccelData(draconicAccelTicks)), "draconic_tick_accel");
		register(new ModuleImpl<>(TICK_ACCEL, CHAOTIC, tickAccelData(chaoticAccelTicks)), "chaotic_tick_accel");
		register(new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(semiStableInstabilityMax, semiStableChaosMax)), "semi_stable_chaos");
		register(new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(stableInstabilityMax, stableChaosMax)), "stable_chaos");
		register(new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(unstableInstabilityMax, unstableChaosMax)), "unstable_chaos");
		register(new ModuleImpl<>(CHAOS_INJECTOR, CHAOTIC, chaosInjectorData(chaosInjectorRate)), "chaos_injector");
	}

	private static Function<Module<AutoFeedData>, AutoFeedData> autoFeedData(float defFoodStorage) {
		return e -> {
			float foodStorage = (float) ModuleCfg.getModuleDouble(e, "food_storage", defFoodStorage);
			return new AutoFeedData(foodStorage);
		};
	}

	private static Function<Module<TickAccelData>, TickAccelData> tickAccelData(int defTickSpeed) {
		return e -> {
			return new TickAccelData(ModuleCfg.getModuleInt(e, "tick_accel", defTickSpeed));
		};
	}
	
	private static Function<Module<StableChaosData>, StableChaosData> stableChaosData(int maxInstability, int defMaxChaos) {
		return e -> {
			return new StableChaosData(
				ModuleCfg.getModuleInt(e, "max_instability", maxInstability),
				ModuleCfg.getModuleInt(e, "max_chaos", defMaxChaos));
		};
	}
	
	private static Function<Module<ChaosInjectorData>, ChaosInjectorData> chaosInjectorData(int defRate) {
		return e -> {
			return new ChaosInjectorData(ModuleCfg.getModuleInt(e, "injection_rate", defRate));
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
