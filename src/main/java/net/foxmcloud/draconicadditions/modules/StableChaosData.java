package net.foxmcloud.draconicadditions.modules;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class StableChaosData implements ModuleData<StableChaosData> {
	private int storedChaos;
	private final int maxChaos;

	public StableChaosData(int maxChaos) {
		this.storedChaos = 0;
		this.maxChaos = maxChaos;
	}

	public StableChaosData(int storedChaos, int maxChaos) {
		this.storedChaos = storedChaos;
		this.maxChaos = maxChaos;
	}

	public int getChaos() {
		return storedChaos;
	}

	public int getMaxChaos() {
		return maxChaos;
	}

	public int addChaos(int chaos) {
		int chaosToAdd = Math.min(getMaxChaos() - getChaos(), chaos);
		storedChaos += chaosToAdd;
		return chaos - chaosToAdd;
	}

	public int removeChaos(int chaos) {
		int chaosToRemove = Math.min(getChaos(), chaos);
		storedChaos -= chaosToRemove;
		return chaosToRemove;
	}

	@Override
	public StableChaosData combine(StableChaosData other) {
		return new StableChaosData(storedChaos + other.storedChaos, maxChaos + other.maxChaos);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context, boolean stack) {
		map.put(new TranslatableComponent("module.draconicadditions.stable_chaos.name"), new TranslatableComponent("module.draconicadditions.stable_chaos.value", storedChaos, maxChaos));
	}
}
