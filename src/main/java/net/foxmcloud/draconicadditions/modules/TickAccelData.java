package net.foxmcloud.draconicadditions.modules;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
	public void addInformation(Map<ITextComponent, ITextComponent> map, ModuleContext context, boolean stack) {
		map.put(new TranslationTextComponent("module.draconicadditions.tick_accel.name"), new TranslationTextComponent("module.draconicadditions.tick_accel.value", ticks));
	}
}
