package net.foxmcloud.draconicadditions.lib;

import static com.brandon3055.brandonscore.api.TechLevel.*;
import static com.brandon3055.draconicevolution.api.modules.ModuleTypes.*;
import static net.foxmcloud.draconicadditions.DAConfig.*;
import static net.foxmcloud.draconicadditions.modules.DAModuleTypes.*;

import java.util.function.Function;

import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.data.AutoFeedData;
import com.brandon3055.draconicevolution.api.modules.items.ModuleItem;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleImpl;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.foxmcloud.draconicadditions.modules.data.TickAccelData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class DAModules {
	public static final DeferredRegister<Module<?>> MODULES = DeferredRegister.create(DEModules.MODULE_KEY, DraconicAdditions.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, DraconicAdditions.MODID);
    
    public static void init(IEventBus modBus) {
        MODULES.register(modBus);
        ITEMS.register(modBus);
    }

	public static final DeferredHolder<Module<?>, Module<?>> chaoticAutoFeed = MODULES.register("chaotic_auto_feed", () -> new ModuleImpl<>(AUTO_FEED, CHAOTIC, autoFeedData((float)chaoticFeedAmount)));
	//public static final DeferredHolder<Module<?>, Module<?>> draconicTickAccel = MODULES.register("draconic_tick_accel", () -> new ModuleImpl<>(TICK_ACCEL, DRACONIC, tickAccelData(draconicAccelTicks)));
	//public static final DeferredHolder<Module<?>, Module<?>> chaoticTickAccel = MODULES.register("chaotic_tick_accel", () -> new ModuleImpl<>(TICK_ACCEL, CHAOTIC, tickAccelData(chaoticAccelTicks)));
	public static final DeferredHolder<Module<?>, Module<?>> semiStableChaos = MODULES.register("semi_stable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(semiStableInstabilityMax, semiStableChaosMax)));
	public static final DeferredHolder<Module<?>, Module<?>> stableChaos = MODULES.register("stable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(stableInstabilityMax, stableChaosMax)));
	public static final DeferredHolder<Module<?>, Module<?>> unstableChaos = MODULES.register("unstable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(unstableInstabilityMax, unstableChaosMax)));
	public static final DeferredHolder<Module<?>, Module<?>> chaosInjector = MODULES.register("chaos_injector", () -> new ModuleImpl<>(CHAOS_INJECTOR, CHAOTIC, chaosInjectorData(chaosInjectorRate)));
	
    public static final DeferredHolder<Item, ModuleItem<?>> itemChaoticAutoFeed   = ITEMS.register("item_chaotic_auto_feed",   () -> new ModuleItem<>(chaoticAutoFeed));
	//public static final DeferredHolder<Item, ModuleItem<?>> itemDraconicTickAccel = ITEMS.register("item_draconic_tick_accel", () -> new ModuleItem<>(draconicTickAccel));
	//public static final DeferredHolder<Item, ModuleItem<?>> itemChaoticTickAccel  = ITEMS.register("item_chaotic_tick_accel",  () -> new ModuleItem<>(chaoticTickAccel));
	public static final DeferredHolder<Item, ModuleItem<?>> itemSemiStableChaos   = ITEMS.register("item_semi_stable_chaos",   () -> new ModuleItem<>(semiStableChaos));
	public static final DeferredHolder<Item, ModuleItem<?>> itemStableChaos       = ITEMS.register("item_stable_chaos",        () -> new ModuleItem<>(stableChaos));
	public static final DeferredHolder<Item, ModuleItem<?>> itemUnstableChaos     = ITEMS.register("item_unstable_chaos",      () -> new ModuleItem<>(unstableChaos));
	public static final DeferredHolder<Item, ModuleItem<?>> itemChaosInjector     = ITEMS.register("item_chaos_injector",      () -> new ModuleItem<>(chaosInjector));

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
				ModuleCfg.getModuleInt(e, "max_chaos", defMaxChaos)
			);
		};
	}
	
	private static Function<Module<ChaosInjectorData>, ChaosInjectorData> chaosInjectorData(int defRate) {
		return e -> {
			return new ChaosInjectorData(ModuleCfg.getModuleInt(e, "injection_rate", defRate));
		};
	}
}
