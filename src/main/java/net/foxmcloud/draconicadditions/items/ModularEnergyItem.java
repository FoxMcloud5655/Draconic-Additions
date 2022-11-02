package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModularEnergyItem extends Item implements IModularItem, IDEEquipment {

	protected final TechLevel techLevel;

	public ModularEnergyItem(TechProperties props) {
		super(props);
		this.techLevel = props.getTechLevel();
	}

	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, 1 + techLevel.index, 1 + techLevel.index, "curios", removeInvalidModules);
		return host;
	}

	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		long capacity = EquipCfg.getBaseEnergy(techLevel);
		return new ModularOPStorage(host, capacity, capacity / 64);
	}

	@Override
	public void initCapabilities(ItemStack stack, ModuleHostImpl host, MultiCapabilityProvider provider) {
		EquipmentManager.addCaps(stack, provider);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		addModularItemInformation(stack, world, tooltip, flags);
	}
}
