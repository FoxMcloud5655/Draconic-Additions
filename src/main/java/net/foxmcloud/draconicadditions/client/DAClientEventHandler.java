package net.foxmcloud.draconicadditions.client;

import java.util.ArrayList;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.client.gui.modular.ModularItemGui;
import com.brandon3055.draconicevolution.client.gui.modular.itemconfig.ConfigurableItemGui;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;

import net.foxmcloud.draconicadditions.client.render.tile.RenderTileFakeReactorComponent;
import net.foxmcloud.draconicadditions.client.render.tile.RenderTileFakeReactorCore;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.DAModuleTypes;
import net.foxmcloud.draconicadditions.modules.entities.ChaosInjectorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

public class DAClientEventHandler {

	public static void init() {
		NeoForge.EVENT_BUS.addListener(DAClientEventHandler::blockChaosItemMoving);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void blockChaosItemMoving(ScreenEvent.MouseButtonPressed.Pre event) {
		if (!(event.getScreen() instanceof AbstractContainerScreen)) {
			return;
		}
		AbstractContainerScreen<?> inventory = (AbstractContainerScreen<?>)event.getScreen();
		Slot slot = inventory.getSlotUnderMouse();
		if (slot == null) {
			return;
		}
		ItemStack stack = slot.getItem();
		if (stack == null) {
			return;
		}
		if ((inventory instanceof ModularItemGui.Screen || inventory instanceof ConfigurableItemGui.Screen) && event.getButton() != 1) {
			return;
		}
		Minecraft mc = inventory.getMinecraft();
		LocalPlayer player = mc.player;
		if (stack.getItem() instanceof ChaosContainer && !player.isCreative() && ((ChaosContainer) stack.getItem()).getChaos(stack) > 0) {
			player.displayClientMessage(Component.translatable("info.da.chaos.cantmove", stack.getHoverName()), true);
			event.setCanceled(true);
		}
		else if (stack.getItem() instanceof IModularArmor && !player.isCreative()) {
			try (ModuleHost host = DECapabilities.getHost(stack)) {
				assert host != null;
				ArrayList<ChaosInjectorEntity> entities = ChaosInjectorEntity.getSortedListFromStream(host.getEntitiesByType(DAModuleTypes.CHAOS_INJECTOR));
				if (entities.isEmpty()) {
					return;
				}
				if (entities.get(0) != null && (entities.get(0).isChaosInBlood() || entities.get(0).getRate() > 0)) {
					player.displayClientMessage(Component.translatable("info.da.chaos.cantmove", stack.getHoverName()), true);
					event.setCanceled(true);
				}
			}
		}
	}
}
