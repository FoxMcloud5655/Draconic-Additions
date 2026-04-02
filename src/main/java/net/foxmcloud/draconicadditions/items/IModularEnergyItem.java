package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.function.Supplier;

import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;

import net.minecraft.world.item.ItemStack;

public interface IModularEnergyItem extends com.brandon3055.draconicevolution.items.equipment.IModularEnergyItem, IDEEquipment {

	@Override
	default ModuleHostImpl instantiateHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(getTechLevel(), 1 + getTechLevel().index, 1 + getTechLevel().index, "curios", removeInvalidModules, ModuleCategory.ENERGY);
		return host;
	}

	@Override
	default ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> hostSupplier) {
		long capacity = EquipCfg.getBaseEnergy(getTechLevel());
		return new ModularOPStorage(hostSupplier, capacity, capacity / 64);
	}
}
