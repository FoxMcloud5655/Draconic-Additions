package net.foxmcloud.draconicadditions.modules.data;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TickAccelData implements ModuleData<TickAccelData> {
	private final int ticks;

	public TickAccelData(int ticks) {
		this.ticks = ticks;
	}

	public int getSpeed() {
		return ticks;
	}

	@Override
	public TickAccelData combine(TickAccelData other) {
		return new TickAccelData(ticks + other.ticks);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context) {
		map.put(new TranslatableComponent("module.draconicadditions.tick_accel.name"), new TranslatableComponent("module.draconicadditions.tick_accel.value", ticks));
	}
}
