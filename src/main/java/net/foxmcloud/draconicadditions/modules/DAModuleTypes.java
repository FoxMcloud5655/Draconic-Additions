package net.foxmcloud.draconicadditions.modules;

import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.types.ModuleTypeImpl;

import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.foxmcloud.draconicadditions.modules.entities.ChaosInjectorEntity;
import net.foxmcloud.draconicadditions.modules.entities.StableChaosEntity;

public class DAModuleTypes {
	//public static final ModuleType<TickAccelData> TICK_ACCEL = new ModuleTypeImpl<>("tick_accel", 1, 2, ModularHarness.HARNESS);
	public static final ModuleType<StableChaosData> STABLE_CHAOS = new ModuleTypeImpl<>("stable_chaos", 3, 5, StableChaosEntity::new, StableChaosEntity.CODEC, StableChaosEntity.STREAM_CODEC, IChaosContainer.CHAOS_CONTAINER);
	public static final ModuleType<ChaosInjectorData> CHAOS_INJECTOR = new ModuleTypeImpl<>("chaos_injector", 2, 2, ChaosInjectorEntity::new, ChaosInjectorEntity.CODEC, ChaosInjectorEntity.STREAM_CODEC, ModuleCategory.CHESTPIECE).setMaxInstallable(1);
	//public static final ModuleType<BombProjectileData> BOMB_PROJECTILE = new ModuleTypeImpl<>("bomb_projectile", 2, 2, BombProjectileEntity::new, ModuleCategory.MELEE_WEAPON).setMaxInstallable(2);
}
