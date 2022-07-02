package net.foxmcloud.draconicadditions.items.curios;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.minecraft.item.ItemStack;

public class ModularNecklace extends ModularEnergyItem {

	public ModularNecklace(TechPropBuilder props) {
		super(props);
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, 1 + techLevel.index, 1 + techLevel.index, "necklace", removeInvalidModules);
		host.addCategories(ModuleCategory.ALL, ModuleCategory.ARMOR_HEAD, ModuleCategory.ARMOR_CHEST);
		return host;
	}

	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		long capacity = (long)(EquipCfg.getBaseEnergy(techLevel) * DAConfig.necklaceCapacityMultiplier);
		return new ModularOPStorage(host, capacity, capacity / 64);
	}
}
