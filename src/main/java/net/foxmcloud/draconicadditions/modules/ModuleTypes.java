package net.foxmcloud.draconicadditions.modules;

import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.types.ModuleTypeImpl;

import net.foxmcloud.draconicadditions.items.curios.ModularHarness;

public class ModuleTypes {
	public static final ModuleType<TickAccelData> TICK_ACCEL = new ModuleTypeImpl<>("tick_accel", 1, 2, ModularHarness.HARNESS);
}
