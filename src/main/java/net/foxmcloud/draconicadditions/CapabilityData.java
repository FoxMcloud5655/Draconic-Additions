package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorInjector;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorStabilizer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CapabilityData {

	public static void init(IEventBus modBus) {
		modBus.addListener(CapabilityData::register);
	}

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		TileFakeReactorStabilizer.register(event);
		TileFakeReactorInjector.register(event);
	}
}
