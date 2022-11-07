package net.foxmcloud.draconicadditions.modules.data;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class StableChaosData implements ModuleData<StableChaosData> {
	private int maxInstability;
	private final int maxChaos;

	public StableChaosData(int maxInstability, int maxChaos) {
		this.maxInstability = maxInstability;
		this.maxChaos = maxChaos;
	}

	public int getMaxInstability() {
		return maxInstability;
	}

	public int getMaxChaos() {
		return maxChaos;
	}

	@Override
	public StableChaosData combine(StableChaosData other) {
		return new StableChaosData(maxInstability + other.maxInstability, maxChaos + other.maxChaos);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context, boolean stack) { //TODO: Remove stack parameter.
		map.put(new TranslatableComponent("module.draconicadditions.maxChaos.name"), new TranslatableComponent("module.draconicadditions.maxChaos.value", maxChaos));
		map.put(new TranslatableComponent("module.draconicadditions.maxInstability.name"), new TranslatableComponent("module.draconicadditions.maxInstability.value", maxInstability));
	}
}
