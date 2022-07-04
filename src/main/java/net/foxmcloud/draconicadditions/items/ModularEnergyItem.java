package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import net.foxmcloud.draconicadditions.DAConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ModularEnergyItem extends Item implements IModularItem, IDEEquipment {

	protected final TechLevel techLevel;

	public ModularEnergyItem(TechPropBuilder props) {
		super(props.build());
		this.techLevel = props.techLevel;
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		addModularItemInformation(stack, worldIn, tooltip, flagIn);
	}
}
