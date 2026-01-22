package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public interface IModularEnergyItem extends IModularItem, IDEEquipment {

	@Override
	default ModuleHostImpl instantiateHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(getTechLevel(), 1 + getTechLevel().index, 1 + getTechLevel().index, "curios", removeInvalidModules);
		return host;
	}

	@Override
	default ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> hostSupplier) {
		long capacity = EquipCfg.getBaseEnergy(getTechLevel());
		return new ModularOPStorage(hostSupplier, capacity, capacity / 64);
	}
}
