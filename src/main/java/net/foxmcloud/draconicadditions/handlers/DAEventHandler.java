package net.foxmcloud.draconicadditions.handlers;

import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.items.ChaosContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DAEventHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
		if (event.getEntityPlayer() != null) {
			float newDigSpeed = event.getOriginalSpeed();
			CustomArmorHandler.ArmorSummery summery = new CustomArmorHandler.ArmorSummery().getSummery(event.getEntityPlayer());
			if (summery == null) {
				return;
			}

			if (event.getEntityPlayer().isInsideOfMaterial(Material.WATER)) {
				if (summery.armorStacks.get(3).getItem() == DAFeatures.chaoticHelm) {
					newDigSpeed *= 5f;
				}
			}

			if (!event.getEntityPlayer().onGround) {
				if (summery.armorStacks.get(2).getItem() == DAFeatures.chaoticChest) {
					newDigSpeed *= 5f;
				}
			}

			if (newDigSpeed != event.getOriginalSpeed()) {
				event.setNewSpeed(newDigSpeed);
			}
		}
	}

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void guiMouse(MouseInputEvent.Pre event) {
		if (event.getGui() instanceof GuiContainer) {
			GuiContainer inventory = (GuiContainer) event.getGui();
			Slot slot = inventory.getSlotUnderMouse();
			if (slot != null) {
				ItemStack stack = slot.getStack();
				if (stack != null && stack.getItem() instanceof ChaosContainer) {
					event.setCanceled(((ChaosContainer) stack.getItem()).getChaos(stack) > 0);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRightClickEntity(EntityInteract event) {
		if (event.getItemStack().getItem() instanceof ChaosContainer) {
			event.getItemStack().getItem().onLeftClickEntity(event.getItemStack(), event.getEntityPlayer(), event.getTarget());
		}
	}
}
