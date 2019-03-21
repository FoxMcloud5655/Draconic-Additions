package net.foxmcloud.draconicadditions.handlers;

import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
}
