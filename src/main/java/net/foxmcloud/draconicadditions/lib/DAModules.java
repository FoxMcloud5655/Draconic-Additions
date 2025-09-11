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
import net.foxmcloud.draconicadditions.modules.data.BombProjectileData;
import net.foxmcloud.draconicadditions.modules.data.BombProjectileData.TYPE;
import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.foxmcloud.draconicadditions.modules.data.TickAccelData;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DAModules {
	public static final DeferredRegister<Module<?>> MODULES = DeferredRegister.create(DEModules.MODULE_KEY, DraconicAdditions.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DraconicAdditions.MODID);
    
    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MODULES.register(eventBus);
        ITEMS.register(eventBus);
    }

	public static final RegistryObject<Module<?>> chaoticAutoFeed = MODULES.register("chaotic_auto_feed", () -> new ModuleImpl<>(AUTO_FEED, CHAOTIC, autoFeedData((float)chaoticFeedAmount)));
	public static final RegistryObject<Module<?>> draconicTickAccel = MODULES.register("draconic_tick_accel", () -> new ModuleImpl<>(TICK_ACCEL, DRACONIC, tickAccelData(draconicAccelTicks)));
	public static final RegistryObject<Module<?>> chaoticTickAccel = MODULES.register("chaotic_tick_accel", () -> new ModuleImpl<>(TICK_ACCEL, CHAOTIC, tickAccelData(chaoticAccelTicks)));
	public static final RegistryObject<Module<?>> semiStableChaos = MODULES.register("semi_stable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(semiStableInstabilityMax, semiStableChaosMax)));
	public static final RegistryObject<Module<?>> stableChaos = MODULES.register("stable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(stableInstabilityMax, stableChaosMax)));
	public static final RegistryObject<Module<?>> unstableChaos = MODULES.register("unstable_chaos", () -> new ModuleImpl<>(STABLE_CHAOS, CHAOTIC, stableChaosData(unstableInstabilityMax, unstableChaosMax)));
	public static final RegistryObject<Module<?>> chaosInjector = MODULES.register("chaos_injector", () -> new ModuleImpl<>(CHAOS_INJECTOR, CHAOTIC, chaosInjectorData(chaosInjectorRate)));
	public static final RegistryObject<Module<?>> bombProjectile = MODULES.register("bomb_projectile", () -> new ModuleImpl<>(BOMB_PROJECTILE, DRACONIC, bombProjectileData(TYPE.NORMAL, 5)));
	public static final RegistryObject<Module<?>> bombProjectileElectric = MODULES.register("bomb_projectile_electric", () -> new ModuleImpl<>(BOMB_PROJECTILE, DRACONIC, bombProjectileData(TYPE.ELECTRIC, 3)));
	public static final RegistryObject<Module<?>> bombProjectileChaotic = MODULES.register("bomb_projectile_chaotic", () -> new ModuleImpl<>(BOMB_PROJECTILE, CHAOTIC, bombProjectileData(TYPE.CHAOTIC, 10)));
	
    public static final RegistryObject<ModuleItem<?>> itemChaoticAutoFeed        = ITEMS.register("item_chaotic_auto_feed",        () -> new ModuleItem<>(chaoticAutoFeed));
	public static final RegistryObject<ModuleItem<?>> itemDraconicTickAccel      = ITEMS.register("item_draconic_tick_accel",      () -> new ModuleItem<>(draconicTickAccel));
	public static final RegistryObject<ModuleItem<?>> itemChaoticTickAccel       = ITEMS.register("item_chaotic_tick_accel",       () -> new ModuleItem<>(chaoticTickAccel));
	public static final RegistryObject<ModuleItem<?>> itemSemiStableChaos        = ITEMS.register("item_semi_stable_chaos",        () -> new ModuleItem<>(semiStableChaos));
	public static final RegistryObject<ModuleItem<?>> itemStableChaos            = ITEMS.register("item_stable_chaos",             () -> new ModuleItem<>(stableChaos));
	public static final RegistryObject<ModuleItem<?>> itemUnstableChaos          = ITEMS.register("item_unstable_chaos",           () -> new ModuleItem<>(unstableChaos));
	public static final RegistryObject<ModuleItem<?>> itemChaosInjector          = ITEMS.register("item_chaos_injector",           () -> new ModuleItem<>(chaosInjector));
	public static final RegistryObject<ModuleItem<?>> itemBombProjectile         = ITEMS.register("item_bomb_projectile",          () -> new ModuleItem<>(bombProjectile));
	public static final RegistryObject<ModuleItem<?>> itemBombProjectileElectric = ITEMS.register("item_bomb_projectile_electric", () -> new ModuleItem<>(bombProjectileElectric));
	public static final RegistryObject<ModuleItem<?>> itemBombProjectileChaotic  = ITEMS.register("item_bomb_projectile_chaotic",  () -> new ModuleItem<>(bombProjectileChaotic));

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
	
	private static Function<Module<BombProjectileData>, BombProjectileData> bombProjectileData(TYPE type, int radius) {
		return e -> {
			return new BombProjectileData(
				TYPE.valueOf(ModuleCfg.getModuleInt(e, "type", type.ordinal())),
				ModuleCfg.getModuleInt(e, "radius", radius)
			);
		};
	}
}
