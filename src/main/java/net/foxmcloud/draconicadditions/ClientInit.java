package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.client.DAClientEventHandler;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosCrystalizer;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosExtractor;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosInfuser;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosLiquifier;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientInit {
	public static void init(IEventBus modBus) {
		modBus.addListener(ClientInit::registerMenuScreensEvent);
		DAClientEventHandler.init();
	}
	
	private static void registerMenuScreensEvent(RegisterMenuScreensEvent event) {
		event.register(DAContent.menuChaosLiquifier.get(), GUIChaosLiquifier.Screen::new);
		event.register(DAContent.menuChaosInfuser.get(), GUIChaosInfuser.Screen::new);
		event.register(DAContent.menuChaosExtractor.get(), GUIChaosExtractor.Screen::new);
		event.register(DAContent.menuChaosCrystalizer.get(), GUIChaosCrystalizer.Screen::new);
	}
}
