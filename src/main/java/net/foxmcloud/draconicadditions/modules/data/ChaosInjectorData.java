package net.foxmcloud.draconicadditions.modules.data;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ChaosInjectorData implements ModuleData<ChaosInjectorData> {
	private final int rate;

	public ChaosInjectorData(int rate) {
		this.rate = rate;
	}

	public int getInjectRate() {
		return rate;
	}

	@Override
	public ChaosInjectorData combine(ChaosInjectorData other) {
		return new ChaosInjectorData(rate + other.rate);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context, boolean stack) {
		map.put(new TranslatableComponent("module.draconicadditions.chaos_injector.name"), new TranslatableComponent("module.draconicadditions.chaos_injector.value", rate));
	}
}
